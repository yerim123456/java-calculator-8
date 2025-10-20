package calculator;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

class ApplicationTest extends NsTest {
    @Test
    void 커스텀_구분자_사용() {
        assertSimpleTest(() -> {
            run("//;\\n1");
            assertThat(output()).contains("결과 : 1");
        });
    }

    @Test
    void 커스텀_기본_구분자_모두_사용() {
        assertSimpleTest(() -> {
            run("//;;\\n1,2:3;;4");
            assertThat(output()).contains("결과 : 10");
        });
    }

    @Test
    void 커스텀_구분자_여러_문자로_지정() {
        assertSimpleTest(() -> {
            run("//;ab\\n1,2:3;ab4");
            assertThat(output()).contains("결과 : 10");
        });
    }

    @Test
    void 커스텀_구분자_문자_숫자_지정() {
        assertSimpleTest(() -> {
            run("//a12\\n1a122,3");
            assertThat(output()).contains("결과 : 6");
        });
    }

    @Test
    void 커스텀_구분자_숫자_문자_지정() {
        assertSimpleTest(() -> {
            run("//12a\\n112a2,3");
            assertThat(output()).contains("결과 : 6");
        });
    }


    @Test
    void 예외_테스트() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("-1,2,3"))
                        .isInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void null_값_전달() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException((String) null))
                        .isInstanceOf(IllegalArgumentException.class));

    }

    @Test
    void 정의되지_않은_구분자_사용() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("1a2;3"))
                        .isInstanceOf(IllegalArgumentException.class));

    }

    @Test
    void 커스텀_구분자_잘못된_정의_방식() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("//'n1:2,3"))
                        .isInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void 커스텀_구분자_적합하지_않은_값_공백() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("// \\n1:2 3"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 커스텀_구분자_적합하지_않은_값_빈값() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("//\\n1:2,3"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 커스텀_구분자_적합하지_않은_값_숫자() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("//12\\n1122,3"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 숫자값_위치에_음수_사용() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("12:-3,4"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 숫자값_위치에_잘못된_문자_사용() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("//;\\n12:a3;4"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 숫자값_위치에_너무_큰값_사용() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("1,9999999999999"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 커스텀_구분자_사이_숫자_값_없음() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("1:2,,3"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
