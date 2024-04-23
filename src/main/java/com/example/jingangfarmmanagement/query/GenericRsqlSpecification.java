package com.example.jingangfarmmanagement.query;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenericRsqlSpecification<T> implements Specification<T> {

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> propertyExpression = parseProperty(root);
        String javaType = String.valueOf(propertyExpression.getJavaType());
        List<Object> args = castArguments(root);
        Object argument = args.get(0);
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {

            case EQUAL:
            {
                if (String.valueOf(argument).equals(SearchConstant.NUMBER_NULL))
                    return builder.isNull(builder.lower(propertyExpression));
                if (String.valueOf(argument).equals(SearchConstant.OBJECT_NULL))
                    return builder.isNull(builder.lower(propertyExpression));
                if (String.valueOf(argument).equals(SearchConstant.OBJECT_NOT_NULL))
                    return builder.isNotNull(builder.lower(propertyExpression));
                if (isDouble(javaType))
                    return builder.equal(propertyExpression, argument);
                if (argument instanceof String) {
                    return builder.like(builder.lower(propertyExpression),
                            formatArgument(argument), '\\');
                } else if (argument == null) {
                    return builder.isNull(root.get(property));
                } else {
                    return builder.equal(root.get(property), argument);
                }
            }
            case NOT_EQUAL: {
                if (argument instanceof String) {
                    return builder.notLike(root.<String> get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNotNull(root.get(property));
                } else {
                    return builder.notEqual(root.get(property), argument);
                }
            }
            case GREATER_THAN: {
                return builder.greaterThan(root.<String> get(property), argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                return builder.greaterThanOrEqualTo(root.<String> get(property), argument.toString());
            }
            case LESS_THAN: {
                return builder.lessThan(root.<String> get(property), argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                return builder.lessThanOrEqualTo(root.<String> get(property), argument.toString());
            }
            case IN:
                return root.get(property).in(args);
            case NOT_IN:
                return builder.not(root.get(property).in(args));
        }

        return null;
    }
    private Boolean isDouble(String javaType) {
        return javaType.equals("class java.lang.Double");
    }
    private Path<String> parseProperty(Root<T> root) {
        Path<String> path;
        if (property.contains(".")) {
            // Nested properties
            String[] pathSteps = property.split("\\.");
            String step = pathSteps[0];
            path = root.get(step);

            for (int i = 1; i <= pathSteps.length - 1; i++) {
                path = path.get(pathSteps[i]);
            }
        } else {
            path = root.get(property);
        }
        return path;
    }

    private String formatArgument(Object argument) {
        String result = argument.toString();
        String HIBERNATE_ESCAPE_CHAR = "\\";
        return result
                .toLowerCase()
                .replace("\\", HIBERNATE_ESCAPE_CHAR + "\\")
                .replace("_", HIBERNATE_ESCAPE_CHAR + "_")
                .replace("%", HIBERNATE_ESCAPE_CHAR + "%")
                .replace('*', '%');
    }
    private List<Object> castArguments(Path<?> propertyExpression) {
        Class<?> type = propertyExpression.getJavaType();

        return arguments.stream().map(arg -> {
            if (type.equals(Integer.class)) return Integer.parseInt(arg);
            else if (type.equals(Long.class)) return Long.parseLong(arg);
            else if (type.equals(Byte.class)) return Byte.parseByte(arg);
            else return arg;
        }).collect(Collectors.toList());
    }

    // standard constructor, getter, setter
}
