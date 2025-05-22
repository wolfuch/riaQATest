package riaTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class getStarted {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            //Navigate and verify the buttons
            page.navigate("https://secure.riamoneytransfer.com/");
            page.waitForLoadState();

            //accept the cookies
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Allow all cookies")).click();

            assertThat(page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Phone or email$")))).isEnabled();
            assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"))).isEnabled();
            assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Register"))).isEnabled();

            //When Register button is clicked, the user is redirected to country selection page.
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Register")).click();
            assertThat(page).hasURL("https://secure.riamoneytransfer.com/registration");


            page.waitForTimeout(1500);



        }
    }
}