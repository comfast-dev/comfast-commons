package dev.comfast.selenium;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
public class PowerTools {
    @Getter
    private final RemoteWebDriver driver;

    /**
     * @return true if element have given css class
     */
    public boolean haveClass(WebElement el, String className) {
        String clazz = el.getAttribute("class");
        return clazz != null && asList(clazz.split(" ")).contains(className);
    }

    /**
     * "unfocus" currently focused element eg. form input
     */
    public void unFocus(WebElement el) {
        getDriver().executeScript("arguments[0].blur();", el);
    }

    /**
     * - 10 times faster that any standard Selenium method
     * - Work even if element is not visible on screen
     */
    @SuppressWarnings("unchecked")
    public List<String> innerTexts(WebElement parent, @Language("CSS") String cssSelector) {
        return (List<String>)getDriver().executeScript(
            "return Array.from(arguments[0].querySelectorAll(arguments[1])).map(el => el.innerText)",
            parent,
            cssSelector);
    }

    /**
     * - 10 times faster that any standard Selenium method
     * - Work even if element is not visible on screen
     */
    @SuppressWarnings("unchecked")
    public List<String> innerTexts(@Language("CSS") String cssSelector) {
        return (List<String>)getDriver().executeScript(
            "return Array.from(document.querySelectorAll(arguments[1])).map(el => el.innerText)",
            cssSelector);
    }

    /**
     * Ignores ElementNotInteractable, ElementNotVisible and similar errors.
     */
    public void clickOver(WebElement el) {
        new Actions(getDriver()).moveToElement(el).click().build().perform();
    }

    /**
     * Fancy effect that highlights element with animation for few seconds.
     */
    public void highlightWithFade(WebElement el) {
        getDriver().executeScript("const el = arguments[0];" +
                "const beforeBackground = el.style.background;\n" +
                "el.style.transition = ''; el.style.background = '#bc00b6';\n" +
                "setTimeout(() => { el.style.transition = 'all 3s ease'; el.style.background = beforeBackground;}, 0)", el);
    }
}
