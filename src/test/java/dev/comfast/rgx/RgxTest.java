package dev.comfast.rgx;

import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.comfast.rgx.RgxApi.rgx;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.DOTALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RgxTest {
    static final String TEXT = "abc def acc";
    static final Rgx passRgx = rgx("a(.)c");
    static final Rgx failRgx = rgx("ccc");

    @Test void simpleRegexTest() {
        var result = rgx(".*def").match(TEXT).get();
        assertThat(result).isEqualTo("abc def");
    }

    @Test void matchAllTest() {
        //when
        List<RgxMatch> matches = passRgx.matchAll(TEXT);

        //then
        assertThat(matches.get(0).get()).isEqualTo("abc");
        assertThat(matches.get(1).get()).isEqualTo("acc");
        assertThat(matches.get(0).group(1)).isEqualTo("b");
        assertThat(matches.get(1).group(1)).isEqualTo("c");
    }

    @Test void throwIfEmptyDoesNotThrowTest() {
        RgxMatch match = passRgx.match(TEXT);

        assertThat(match.throwIfEmpty().get()).isEqualTo("abc");
        assertThat(match.throwIfEmpty().group(1)).isEqualTo("b");
        assertThat(match.throwIfEmpty().isPresent()).isTrue();

        assertThat(match.throwIfEmpty("oh no").get()).isEqualTo("abc");
        assertThat(match.throwIfEmpty("oh no").group(1)).isEqualTo("b");
        assertThat(match.throwIfEmpty("oh no").isPresent()).isTrue();
    }

    @Test void matchAllAsStringTest() {
        //when
        List<String> strings = passRgx.matchAllAsString(TEXT);

        //then
        assertThat(strings.get(0)).isEqualTo("abc");
        assertThat(strings.get(1)).isEqualTo("acc");
    }

    @Test void matchAllNthGroupTest() {
        //when
        List<String> groups = passRgx.matchAllAsString(TEXT, 1);

        //then
        assertThat(groups.get(0)).isEqualTo("b");
        assertThat(groups.get(1)).isEqualTo("c");
    }

    @Test void notMatchFail() {
        assertThatCode(() -> failRgx.match(TEXT))
            .doesNotThrowAnyException();

        var errorMsg = "Not found pattern 'ccc' in text:";
        //get()
        assertThatThrownBy(() -> failRgx.match(TEXT).get())
            .isInstanceOf(RgxNotFound.class)
            .hasMessageContaining(errorMsg);

        //group(#)
        assertThatThrownBy(() -> failRgx.match(TEXT).group(2))
            .isInstanceOf(RgxNotFound.class)
            .hasMessageContaining(errorMsg);

        //throwIfEmpty()
        assertThatThrownBy(() -> failRgx.match(TEXT).throwIfEmpty())
            .isInstanceOf(RgxNotFound.class)
            .hasMessageContaining(errorMsg);

        //throwIfEmpty(msg)
        assertThatThrownBy(() -> failRgx.match(TEXT).throwIfEmpty("oh no !!!"))
            .isInstanceOf(RgxNotFound.class)
            .hasMessage("oh no !!!")
            .cause().isInstanceOf(RgxNotFound.class)
            .hasMessageContaining(errorMsg);
    }

    @Test void notFoundGroupFail() {
        assertThatThrownBy(() -> passRgx.match(TEXT).group(3))
            .isInstanceOf(RgxNotFound.class)
            .hasMessageContaining("Match doesn't contain group #3 in 1 total groups");
    }

    @Test void flagsUsage() {
        var result = rgx("ABC.+DEF", CASE_INSENSITIVE | DOTALL).match("abc \n def").get();

        assertThat(result).isEqualTo("abc \n def");
    }
}