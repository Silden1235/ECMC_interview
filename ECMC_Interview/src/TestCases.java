import java.time.Duration;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestCases {
	
	WebDriver driver = null;
	
	@BeforeTest
	public void setUpTest()
	{
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	@Test
	public void Test_WaitingButton()
	{
		// Wait for page load and click enable button
		driver.get("https://interview-app-plum.vercel.app/");
		driver.findElement(By.cssSelector("a[href='/waiting']")).click();
		driver.findElement(By.cssSelector("button[data-testid='enableShowAlertButton']")).click();
		// Wait until button is able to be clicked
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
		w.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-testid='ShowAlertButton']")));
		driver.findElement(By.cssSelector("button[data-testid='ShowAlertButton']")).click();
		// Check result in alert
		Assert.assertEquals(driver.switchTo().alert().getText(),"False alarm!");
		driver.switchTo().alert().accept();
	}
	
	
	@Test
	public void Test_WaitingButtonAccorion()
	{
		// Click and fill out accordion calues
		driver.get("https://interview-app-plum.vercel.app/");
		driver.findElement(By.cssSelector("a[href='/waiting']")).click();
		driver.findElement(By.cssSelector("button[data-testid='enableShowAlertButton']")).click();
		driver.findElement(By.xpath("//button[contains(.,'Accordion Item #1')]")).click();
		driver.findElement(By.id("accordion1Text")).sendKeys("Hello");
		driver.findElement(By.xpath("//button[contains(.,'Accordion Item #2')]")).click();
		driver.findElement(By.id("accordion2Text")).sendKeys("World");
		driver.findElement(By.xpath("//button[contains(.,'Result')]")).click();
		driver.findElement(By.cssSelector("button[name='accordionMessageButton']")).click();
		// Check result in alert
		Assert.assertEquals(driver.switchTo().alert().getText(),"Hello World");
		driver.switchTo().alert().accept();
	}
	
	@Test
	public void Test_FillFormHappyPath() throws InterruptedException 
	{
		// Get Form and fill out
		driver.get("https://interview-app-plum.vercel.app/");
		driver.findElement(By.cssSelector("a[href='/contact']")).click();
		driver.findElement(By.id("firstName")).sendKeys("John");
		driver.findElement(By.id("lastName")).sendKeys("Smith");
		driver.findElement(By.cssSelector("input[placeholder = 'City']")).sendKeys("Pleasantville");
		WebElement stateElement = driver.findElement(By.xpath("//select[@id='addressState']"));
		Select stateOptions = new Select(stateElement);
		stateOptions.selectByIndex(1);
		driver.findElement(By.id("addressZip")).sendKeys("55812");
		driver.findElement(By.xpath("//input[@type='checkbox']")).click();
		driver.findElement(By.cssSelector("#messageBox")).sendKeys("Lorem ipsum dolor sit amet, "
				+ "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
				+ "Nisl condimentum id venenatis a condimentum. Phasellus egestas tellus rutrum tellus.");
		// Submit Form
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		// Verify Result 
		Thread.sleep(3000); // NOT ideal but Google was not helping
		Assert.assertEquals(driver.findElement(By.xpath("//h2")).getText(),"Thank you for contacting us, John!");
	}
	
	@Test
	public void Test_ErrorForm()
	{
		// Get Form and fill in first and last name
		driver.get("https://interview-app-plum.vercel.app/");
		driver.findElement(By.cssSelector("a[href='/contact']")).click();
		driver.findElement(By.id("firstName")).sendKeys("John");
		driver.findElement(By.id("lastName")).sendKeys("Smith");
		// Submit Form
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		// Verify Results 
		String firstNameText = driver.findElement(By.xpath("//div[1]//div[1]//div[1]")).getText();
		String acceptTermsText = driver.findElement(By.cssSelector("div[class='form-check'] div[class='invalid-feedback']")).getText();
		Assert.assertEquals(firstNameText, "Looks good!");
		Assert.assertEquals(acceptTermsText, "You must agree before submitting.");
	}
	
	@AfterTest
	public void tearDownTest()
	{
		driver.close();
		driver.quit();
		System.out.println("Test completed successfully");
	}
	
}
