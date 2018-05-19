
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class FullCalendarView
{

	private static ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
	private HBox view;
	private static Text calendarTitle;

	/**
	 * Create a calendar view
	 * 
	 * @param yearMonth
	 *            year month to create the calendar of
	 */
	public FullCalendarView(YearMonth yearMonth, Controller con)
	{
		Globals.con = con;

		Globals.currentYearMonth = yearMonth;
		con.addButton.setOnAction(e ->
		{
			Main.showAddBill();
		});

		// Create the calendar grid pane
		GridPane calendar = new GridPane();
		calendar.setPrefSize(700, 500);
		calendar.setGridLinesVisible(true);
		// Create rows and columns with anchor panes for the calendar
		for (int i = 0; i < 5; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				AnchorPaneNode ap = new AnchorPaneNode();
				ap.setPrefSize(100, 100);

				calendar.add(ap, j, i);
				allCalendarDays.add(ap);
			}
		}
		// Days of the week labels
		Text[] dayNames = new Text[] { new Text("         Sunday"), new Text("        Monday"), new Text("       Tuesday"), new Text("     Wednesday"), new Text("        Thursday"), new Text("          Friday"),
				new Text("        Saturday") };
		GridPane dayLabels = new GridPane();
		dayLabels.setPrefWidth(600);
		Integer col = 0;

		for (Text txt : dayNames)
		{

			AnchorPane ap = new AnchorPane();
			ap.setPrefSize(100, 10);
			txt.setTextAlignment(TextAlignment.CENTER);
			AnchorPane.setBottomAnchor(txt, 5.0);

			ap.getChildren().add(txt);
			dayLabels.add(ap, col++, 0);
		}
		// Create calendarTitle and buttons to change current month
		calendarTitle = new Text();
		calendarTitle.prefWidth(100);
		Label spacer1 = new Label("   ");
		Label spacer2 = new Label("   ");

		Button previousMonth = new Button("<<");
		previousMonth.setOnAction(e -> previousMonth());
		Button nextMonth = new Button(">>");
		nextMonth.setOnAction(e -> nextMonth());

		HBox titleBar = new HBox(previousMonth, spacer1, calendarTitle, spacer2, nextMonth);
		titleBar.setAlignment(Pos.BOTTOM_CENTER);

		// Populate calendar with the appropriate day numbers
		// populateCalendar(yearMonth);

		// Get the date we want to start with on the calendar
		LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
		// Dial back the day until it is SUNDAY (unless the month starts on a sunday)
		while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY"))
		{
			calendarDate = calendarDate.minusDays(1);
		}
		// Populate the calendar with day numbers
		for (AnchorPaneNode ap : allCalendarDays)
		{

			if (ap.getChildren().size() != 0)
			{
				ap.getChildren().clear();

			}

			ap.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

			Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
			txt.setFont(new Font(20));

			ap.setDate(calendarDate);

			ap.getChildren().add(txt);

			ap.setDate(calendarDate);

			AnchorPaneNode.setTopAnchor(txt, 5.0);
			AnchorPaneNode.setLeftAnchor(txt, 5.0);

			calendarDate = calendarDate.plusDays(1);
		}
		// Change the title of the calendar
		calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
		// Create the calendar view
		Label spacer = new Label("         ");
		Label spacer3 = new Label("         ");
		Label spacer4 = new Label("         ");
		Label spacer5 = new Label("         ");

		VBox calendarview = new VBox(spacer, spacer3, spacer4, titleBar, spacer5, dayLabels, calendar);

		view = new HBox(spacer, calendarview);

	}

	/**
	 * Set the days of the calendar to correspond to the appropriate date
	 * 
	 * @param yearMonth
	 *            year and month of month to render
	 */
	public static void populateCalendar(YearMonth yearMonth)
	{
		// Get the date we want to start with on the calendar
		LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
		// Dial back the day until it is SUNDAY (unless the month starts on a sunday)
		while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY"))
		{
			calendarDate = calendarDate.minusDays(1);
		}
		// Populate the calendar with day numbers
		for (AnchorPaneNode ap : allCalendarDays)
		{

			if (ap.getChildren().size() != 0)
			{
				ap.getChildren().clear();

			}

			Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
			txt.setFont(new Font(20));

			ap.setDate(calendarDate);

			AnchorPaneNode.setTopAnchor(txt, 5.0);
			AnchorPaneNode.setLeftAnchor(txt, 5.0);

			ap.setPadding(new Insets(5));
			ap.getChildren().add(txt);
			calendarDate = calendarDate.plusDays(1);
		}
		for (AnchorPaneNode ap : allCalendarDays)
		{
			if (LoginController.props.getProperty(ap.getDate().toString()) != null)
			{
				ArrayList<Bill> data = Main.stringToArrayList(LoginController.props.getProperty(ap.getDate().toString()));
				ap.loadData(data);
			}
			else
			{
				ArrayList<Bill> data = new ArrayList<Bill>();
				ap.loadData(data);
			}
			ap.getChildren().removeAll();
			VBox Vbox = new VBox();
			int x = 0;
			for (Bill bill : ap.getData())
			{
				x++;
				if (!(x > 3))
				{
					Text mes = new Text(bill.name);
					mes.setFont(new Font(10.5));
					mes.setWrappingWidth(80);
					mes.setTextAlignment(TextAlignment.JUSTIFY);
					Vbox.getChildren().add(mes);
				}
				else if (x == 4 && ap.getData().size() > 4)
				{
					Text mes = new Text("More...");
					mes.setFont(new Font(10.5));
					mes.setWrappingWidth(80);
					mes.setTextAlignment(TextAlignment.JUSTIFY);
					Vbox.getChildren().add(mes);
				}
				else if (x == 4)
				{
					Text mes = new Text(bill.name);
					mes.setFont(new Font(10.5));
					mes.setWrappingWidth(80);
					mes.setTextAlignment(TextAlignment.JUSTIFY);
					Vbox.getChildren().add(mes);
				}

			}
			AnchorPaneNode.setTopAnchor(Vbox, 30.0);
			AnchorPaneNode.setLeftAnchor(Vbox, 5.0);
			ap.getChildren().add(Vbox);
		}
		highlightToday();
		// Change the title of the calendar
		calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));

	}

	public static void highlightToday()
	{
		LocalDate calendarDate = LocalDate.now();
		for (AnchorPaneNode ap : allCalendarDays)
		{

			if (ap.getDate().equals(calendarDate) && !ap.equals(Globals.selected))
			{

				ap.setBackground(new Background(new BackgroundFill(LoginController.getColor("Today_Color"), CornerRadii.EMPTY, Insets.EMPTY)));
				setToday(ap);
			}
			else if (ap.equals(Globals.selected))
			{

			}
			else
			{
				ap.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
			}

		}

	}

	public static void updateData()
	{
		populateCalendar(Globals.currentYearMonth);
		updateSelected();

	}

	/**
	 * Move the month back by one. Repopulate the calendar with the correct dates.
	 */
	public static void resetGridColor()
	{
		for (AnchorPaneNode ap : allCalendarDays)
		{
			ap.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		highlightToday();
	}

	private void previousMonth()
	{
		Globals.currentYearMonth = Globals.currentYearMonth.minusMonths(1);
		populateCalendar(Globals.currentYearMonth);
		updateSelected();
	}

	/**
	 * Move the month forward by one. Repopulate the calendar with the correct dates.
	 */
	private void nextMonth()
	{
		Globals.currentYearMonth = Globals.currentYearMonth.plusMonths(1);
		populateCalendar(Globals.currentYearMonth);
		updateSelected();

	}

	public HBox getView()
	{
		return view;
	}

	public ArrayList<AnchorPaneNode> getAllCalendarDays()
	{
		return allCalendarDays;
	}

	public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays)
	{
		FullCalendarView.allCalendarDays = allCalendarDays;
	}

	@SuppressWarnings("unchecked")
	public static void updateSelected()
	{

		Globals.con.dateLabel.setText(Globals.selected.getDate().toString());

		Globals.con.listView.getItems().removeAll(Globals.con.listView.getItems());

		Globals.con.HL.setText("");

		Globals.con.nameInfo.setText("");
		Globals.con.companyInfo.setText("");
		Globals.con.dateInfo.setText("");
		Globals.con.amountInfo.setText("");
		Globals.con.otherInfo.setText("");

		Globals.con.HL.setDisable(true);
		Globals.con.editButton.setDisable(true);
		Globals.con.deleteButton.setDisable(true);
		Globals.con.Paid.setDisable(true);
		Globals.con.Paid.setSelected(false);
		
		if (LoginController.props.getProperty(Globals.selected.getDate().toString()) != null)
		{
			ArrayList<Bill> data = Main.stringToArrayList(LoginController.props.getProperty(Globals.selected.getDate().toString()));
			Globals.selected.loadData(data);
			if (Globals.selected.getData().size() > 0)
			{

				for (Bill data1 : Globals.selected.getData())
				{
					Globals.con.listView.getItems().add(data1.name);

				}
			}

		}

	}

	public static void jumpTo(YearMonth yearMonth)
	{
		Globals.currentYearMonth = yearMonth;
		populateCalendar(Globals.currentYearMonth);
		resetGridColor();
		updateSelected();

	}

	public static AnchorPaneNode getToday()
	{
		return Globals.today;
	}

	public static void setToday(AnchorPaneNode today)
	{
		Globals.today = today;
	}

	public static int getUnpaidCount(AnchorPaneNode day)
	{
		int count = 0;
		for (Bill bill : day.getData())
		{
			if (!bill.paid.equals("true"))
			{
				count++;
			}
		}
		return count;
	}

	public static void addProp(String name, String list)
	{
		try
		{
			Main.input = new FileInputStream(LoginController.filePath);

			try
			{
				Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
				aes.init(Cipher.DECRYPT_MODE, LoginController.secretKeySpec);
				Main.in = new CipherInputStream(Main.input, aes);
				LoginController.props.load(Main.in);
				Main.in.close();
				Main.input.close();
			}
			catch (InvalidKeyException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchPaddingException e)
			{
				e.printStackTrace();
			}

		}
		catch (FileNotFoundException e2)
		{
			e2.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			Main.output = new FileOutputStream(LoginController.filePath);

			try
			{
				Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

				aes.init(Cipher.ENCRYPT_MODE, LoginController.secretKeySpec);

				Main.out = new CipherOutputStream(Main.output, aes);

				LoginController.props.setProperty(name, list);
				try
				{
					LoginController.props.store(Main.out, null);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				updateSelected();
				Main.out.close();
				Main.output.close();
			}
			catch (InvalidKeyException e1)
			{

				e1.printStackTrace();
			}
			catch (NoSuchAlgorithmException e1)
			{
				e1.printStackTrace();
			}
			catch (NoSuchPaddingException e1)
			{
				e1.printStackTrace();
			}

		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void saveBackup(String path)
	{
		if (path != null && !path.equals(""))
		{
			File directory = new File(path);
			File propfilecheck = new File(path + LoginController.fileName);

			if (!directory.exists() || !propfilecheck.exists())
			{
				directory.mkdir();
				try
				{
					Main.input = new FileInputStream(LoginController.filePath);

					try
					{
						Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
						aes.init(Cipher.DECRYPT_MODE, LoginController.secretKeySpec);
						Main.in = new CipherInputStream(Main.input, aes);
						LoginController.props.load(Main.in);
						Main.in.close();
						Main.input.close();
					}
					catch (InvalidKeyException e)
					{
						e.printStackTrace();
					}
					catch (NoSuchAlgorithmException e)
					{
						e.printStackTrace();
					}
					catch (NoSuchPaddingException e)
					{
						e.printStackTrace();
					}

				}
				catch (FileNotFoundException e2)
				{
					e2.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				try
				{
					Main.output = new FileOutputStream(path + "\\" + LoginController.fileName);

					try
					{
						Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

						aes.init(Cipher.ENCRYPT_MODE, LoginController.secretKeySpec);

						Main.out = new CipherOutputStream(Main.output, aes);
						try
						{
							LoginController.props.store(Main.out, null);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						updateSelected();
						Main.out.close();
						Main.output.close();
					}
					catch (InvalidKeyException e1)
					{

						e1.printStackTrace();
					}
					catch (NoSuchAlgorithmException e1)
					{
						e1.printStackTrace();
					}
					catch (NoSuchPaddingException e1)
					{
						e1.printStackTrace();
					}

				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					Main.input = new FileInputStream(LoginController.filePath);

					try
					{
						Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
						aes.init(Cipher.DECRYPT_MODE, LoginController.secretKeySpec);
						Main.in = new CipherInputStream(Main.input, aes);
						LoginController.props.load(Main.in);
						Main.in.close();
						Main.input.close();
					}
					catch (InvalidKeyException e)
					{
						e.printStackTrace();
					}
					catch (NoSuchAlgorithmException e)
					{
						e.printStackTrace();
					}
					catch (NoSuchPaddingException e)
					{
						e.printStackTrace();
					}

				}
				catch (FileNotFoundException e2)
				{
					e2.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				try
				{
					Main.output = new FileOutputStream(path + "\\" + LoginController.fileName);

					try
					{
						Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

						aes.init(Cipher.ENCRYPT_MODE, LoginController.secretKeySpec);

						Main.out = new CipherOutputStream(Main.output, aes);
						try
						{
							LoginController.props.store(Main.out, null);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						updateSelected();
						Main.out.close();
						Main.output.close();
					}
					catch (InvalidKeyException e1)
					{

						e1.printStackTrace();
					}
					catch (NoSuchAlgorithmException e1)
					{
						e1.printStackTrace();
					}
					catch (NoSuchPaddingException e1)
					{
						e1.printStackTrace();
					}

				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	public static void removeProp(String name)
	{
		try
		{
			Main.input = new FileInputStream(LoginController.filePath);

			try
			{
				Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
				aes.init(Cipher.DECRYPT_MODE, LoginController.secretKeySpec);
				Main.in = new CipherInputStream(Main.input, aes);
				LoginController.props.load(Main.in);
				Main.in.close();
				Main.input.close();
			}
			catch (InvalidKeyException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchPaddingException e)
			{
				e.printStackTrace();
			}

		}
		catch (FileNotFoundException e2)
		{
			e2.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			Main.output = new FileOutputStream(LoginController.filePath);

			try
			{
				Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

				aes.init(Cipher.ENCRYPT_MODE, LoginController.secretKeySpec);

				Main.out = new CipherOutputStream(Main.output, aes);

				LoginController.props.remove(name);
				try
				{
					LoginController.props.store(Main.out, null);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				updateSelected();
				Main.out.close();
				Main.output.close();
			}
			catch (InvalidKeyException e1)
			{

				e1.printStackTrace();
			}
			catch (NoSuchAlgorithmException e1)
			{
				e1.printStackTrace();
			}
			catch (NoSuchPaddingException e1)
			{
				e1.printStackTrace();
			}

		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
