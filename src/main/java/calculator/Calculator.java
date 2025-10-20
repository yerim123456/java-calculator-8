package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static final String DEFAULT_DELIMITERS = ",|:";
    private static final String CUSTOM_DELIMITERS_REGEX = "^//(.+)\\\\n(.*)";

    /**
     * 문자열에서 숫자를 추출하고 더한 결과를 반환하는 메서드
     *
     * @param input 입력 문자열
     * @return 더한 결과 값
     */
    public static int add(String input) {
        // 3. 문자열이 비어있는 경우 0 반환
        if (isEmpty(input)) {
            return 0;
        }

        // 기본 구분자로 구분자 설정
        String delimiters = DEFAULT_DELIMITERS;

        // 기본 숫자 부분 설정
        String numbers = input;

        // 4. 커스텀 구분자 유무에 따른 유효성 검사
        if (input.startsWith("//")) {
            // 5. 커스텀 구분자 추출
            delimiters = extractCustomDelimiter(input);

            // 6. 구분자에 따라 더할 숫자 추출
            numbers = extractNumbersAfterDelimiter(input);

            // 추출된 숫자 없는 경우
            if (numbers == null) {
                return 0;
            }
        }

        // 8. 추출한 숫자 합산
        return calculateSum(numbers, delimiters);
    }

    /**
     * 입력된 문자열이 공백 또는 null인지 검사
     *
     * @param input 입력 문자열
     * @return 공백 문자열 여부
     */
    private static boolean isEmpty(String input) {
        return input.trim().isEmpty();
    }

    /**
     * 문자열에서 커스텀 구분자를 추출
     *
     * @param input 입력 문자열
     * @return 커스텀 구분자
     */
    private static String extractCustomDelimiter(String input) {
        // // 와 \n 사이의 값 추출
        Matcher matcher = Pattern.compile(CUSTOM_DELIMITERS_REGEX).matcher(input);

        if (matcher.find()) {
            // 찾은 첫번째 구분자와 기본 구분자 함께 반환
            String customDelimiter = matcher.group(1);

            // 공백 또는 빈값이거나 숫자로만 이루어진 값의 경우
            if (customDelimiter.trim().isEmpty() || customDelimiter.matches("\\d+")) {
                throw new IllegalArgumentException("커스텀 구분자로 적합하지 않습니다.");
            }

            return Pattern.quote(customDelimiter) + "|" + DEFAULT_DELIMITERS;
        } else {
            // 그 외의 경우 커스텀 구분자 형식이 잘못됨을 알림(ex) //a > \n이 빠진 경우 )
            throw new IllegalArgumentException("커스텀 구분자 방식이 잘못되었습니다.");
        }
    }

    /**
     * 문자열에서 커스텀 구분자 이후의 숫자 부분을 추출
     *
     * @param input 입력 문자열
     * @return 숫자 부분 문자열 또는 null
     */
    private static String extractNumbersAfterDelimiter(String input) {
        // //{커스텀 구분자}\n 이후의 모든 문자열 추출
        Matcher matcher = Pattern.compile(CUSTOM_DELIMITERS_REGEX).matcher(input);

        if (matcher.find()) {
            // 커스텀 구분자 지정 부분 제외하고 반환
            return matcher.group(2);
        }

        // 그 외의 경우 커스텀 구분자 이후에 숫자가 입력되지 않음을 알림
        return null;
    }

    /**
     * 주어진 문자열을 구분자를 기준으로 분리하고 숫자를 더한 결과를 반환
     *
     * @param input      입력 문자열 (숫자들)
     * @param delimiters 사용되는 구분자들
     * @return 숫자들의 합 (최종 결과값)
     */
    private static int calculateSum(String input, String delimiters) {
        // 기본/커스텀 구분자 기준으로 분리
        String[] tokens = input.split(delimiters);
        int sum = 0;

        for (String token : tokens) {
            if (!token.isEmpty()) {
                try {
                    int number = Integer.parseInt(token);

                    // 7. 숫자 유효성 검사_음수 여부
                    if (number < 0) {
                        throw new IllegalArgumentException("음수는 계산할 수 없습니다.");
                    }

                    sum += number;

                } catch (NumberFormatException e) { // 7. 숫자 유효성 검사_문자가 포함되어 있거나, 너무 큰 값인 경우
                    throw new IllegalArgumentException("숫자로 변환이 어렵습니다");
                }
            } else { // 7. 숫자 유효성 검사_구분자와 구분자 사이에 공백인 경우
                throw new IllegalArgumentException("구분자와 구분자 사이에는 숫자가 필요합니다.");
            }
        }

        return sum;
    }

}
