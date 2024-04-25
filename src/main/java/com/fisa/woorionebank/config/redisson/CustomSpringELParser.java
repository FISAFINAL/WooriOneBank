package com.fisa.woorionebank.config.redisson;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Spring Expression Language Parser
 * 전달받은 Lock의 이름을 Spring Expression Language 로 파싱하여 읽어옴.
 */
public class CustomSpringELParser {
    private CustomSpringELParser() {
    }

    // 락의 이름과 관련된 Spring Expression Language 표현식을 전달받음.
    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        //SpEL을 사용하여 락의 이름에 포함된 SpEL 표현식을 평가
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        //메서드의 시그니처에 따라 동적으로 락의 이름을 생성하고, SpEL을 활용하여 특정 조건에 따라 락을 설정할 수 있습니다.
        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
