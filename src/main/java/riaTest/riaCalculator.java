package riaTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class riaCalculator {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            //Navigate and verify the buttons
            page.navigate("https://www.riamoneytransfer.com/en-cl/");
            page.waitForLoadState();
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^You sendCLP$"))).getByPlaceholder("0").click();
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^You sendCLP$"))).getByPlaceholder("0").fill("");
            assertThat(page.getByText("Send amount must be between 1 and")).isVisible();
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^You sendCLP$"))).getByPlaceholder("0").fill("100.000");
            page.waitForTimeout(1500);

            //Fill the info and click and verify the transaction
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Send to Colombia$"))).nth(3).click();
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Haiti$"))).first().click();

            //Verify conversion happens
            assertThat(page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Recipient getsHTG$"))).getByPlaceholder("0")).hasValue("13.746");
            page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^You sendCLP$"))).getByPlaceholder("0").fill("25000");
            assertThat(page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Recipient getsHTG$"))).getByPlaceholder("0")).hasValue("3.436");

            //Verify "get started" redirect to other url
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get started")).click();
            page.waitForLoadState();
            assertThat(page).hasURL("https://secure.riamoneytransfer.com/");

        }
    }
}