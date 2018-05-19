
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class Main extends Application
{
	public static Stage primaryStage;
	public static Stage LoginStage;
	public static Stage addBill;
	public static Stage about;
	public static Stage settings;

	public static OutputStream output = null;
	public static FileInputStream input = null;

	public static CipherOutputStream out;
	public static CipherInputStream in;

	public static NewBillController addBillcontroller;
	public static settingsController settingsController;
	public static LoginController loginController;

	public static void main(String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException
	{
		LoginStage = new Stage();
		primaryStage = new Stage();
		addBill = new Stage();
		about = new Stage();
		settings = new Stage();

		// Create the FXMLLoader
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		LoginStage.setTitle("Bill Calendar");
		Scene scene = new Scene(loader.load());
		LoginStage.setScene(scene);
		LoginStage.initStyle(StageStyle.UNDECORATED);
		LoginStage.getIcons().add(new Image(Main.class.getResourceAsStream("bill.png")));
		loginController = loader.getController();

		FXMLLoader loader1 = new FXMLLoader(getClass().getResource("fullCalendar.fxml"));
		primaryStage.setTitle("Bill Calendar");
		Scene primary = new Scene(loader1.load());
		primary.setFill(Color.OLDLACE);
		primaryStage.setScene(primary);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("bill.png")));

		Controller controller = loader1.getController();
		MenuBar menuBar = new MenuBar();

		// --- Menu File
		Menu menuFile = new Menu("File");

		MenuItem close = new MenuItem("Exit");
		close.setOnAction(e ->
		{
			Platform.exit();
		});

		SeparatorMenuItem split = new SeparatorMenuItem();

		MenuItem settingsItem = new MenuItem("Settings");

		settingsItem.setOnAction(e ->
		{
			Main.showSettings();

		});

		menuFile.getItems().addAll(settingsItem, split, close);

		// --- Menu Edit

		// --- Menu View
		Menu menuHelp = new Menu("Help");

		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(e ->
		{
			showAbout();
		});

		menuHelp.getItems().addAll(aboutItem);

		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		menuBar.getMenus().addAll(menuFile, menuHelp);

		controller.calendarPane.getChildren().add(new FullCalendarView(YearMonth.now(), controller).getView());
		controller.calendarPane.getChildren().add(menuBar);

		FXMLLoader addBillloader1 = new FXMLLoader(getClass().getResource("createBill.fxml"));
		addBill.setTitle("Bill Calendar");
		addBill.setScene(new Scene(addBillloader1.load()));
		addBill.getIcons().add(new Image(Main.class.getResourceAsStream("bill.png")));
		addBill.initStyle(StageStyle.UNIFIED);

		addBill.setOnCloseRequest(e ->
		{
			Main.addBillcontroller.nameField.setText("");
			Main.addBillcontroller.otherField.setText("");
			Main.addBillcontroller.linkField.setText("");
			Main.addBillcontroller.comField.setText("");
			Main.addBillcontroller.amountField.setText("");
			Main.addBillcontroller.paidToggle.setSelected(false);
			Main.addBillcontroller.isEdit = false;
		});

		NewBillController addBillcontroller = addBillloader1.getController();

		Main.addBillcontroller = addBillcontroller;

		FXMLLoader aboutloader1 = new FXMLLoader(getClass().getResource("about.fxml"));
		about.setTitle("Bill Calendar");
		about.setScene(new Scene(aboutloader1.load()));
		about.getIcons().add(new Image(Main.class.getResourceAsStream("bill.png")));
		about.initStyle(StageStyle.UNIFIED);
		about.setResizable(false);

		FXMLLoader settingsloader1 = new FXMLLoader(getClass().getResource("Settings.fxml"));
		settings.setTitle("Bill Calendar");
		settings.setScene(new Scene(settingsloader1.load()));
		settings.getIcons().add(new Image(Main.class.getResourceAsStream("bill.png")));
		settings.initStyle(StageStyle.UNIFIED);
		settings.setResizable(false);

		settingsController settingsController = settingsloader1.getController();

		Main.settingsController = settingsController;

		settings.setOnShowing(e ->
		{
			Main.settingsController.selectColor.setValue(Color.web(LoginController.props.getProperty("Select_Color")));
			Main.settingsController.todayColor.setValue(Color.web(LoginController.props.getProperty("Today_Color")));
			Main.settingsController.useNotifs.setSelected(getBool(LoginController.props.getProperty("Use_Noti")));
			Main.settingsController.urlBar.setText(LoginController.props.getProperty("Backup_URL"));
		});

		LoginStage.show();

		primaryStage.setOnCloseRequest(e ->
		{
			try
			{
				Main.closeAll();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});

	}

	public static void close()
	{
		LoginStage.close();
	}

	public static void moveWindow(double x, double y)
	{
		LoginStage.setX(x);
		LoginStage.setY(y);
	}

	public static void showError(Exception e)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Error!");
		alert.setContentText("Error!");

		Exception ex = e;

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	public static void showPrimary()
	{

		FullCalendarView.highlightToday();
		Globals.selected = FullCalendarView.getToday();
		FullCalendarView.resetGridColor();
		Globals.selected.setBackground(new Background(new BackgroundFill(LoginController.getColor("Select_Color"), CornerRadii.EMPTY, Insets.EMPTY)));
		FullCalendarView.updateSelected();
		FullCalendarView.updateData();
		if (Main.getBool(LoginController.props.get("Use_Noti").toString()) && FullCalendarView.getToday().getData().size() > 0 && FullCalendarView.getUnpaidCount(FullCalendarView.getToday()) > 0)
		{
			TrayNotification tray = new TrayNotification();
			tray.setTitle("Bill Calendar");
			tray.setMessage("You have bills that are due today.");
			tray.setNotificationType(NotificationType.NOTICE);
			tray.setTrayIcon(new Image(Main.class.getResourceAsStream("bill.png")));
			tray.setAnimationType(AnimationType.FADE);
			tray.setRectangleFill(Paint.valueOf("#d7f442"));
			tray.showAndDismiss(Duration.seconds(5));
		}
		primaryStage.show();
		primaryStage.toFront();
		Main.closeLogin();
	}

	public static void showAddBill()
	{
		Main.addBillcontroller.dateField.setValue(Globals.selected.getDate());
		addBill.show();
		addBill.toFront();

	}

	public static void showSettings()
	{
		settings.show();
		settings.toFront();
	}

	public static void showAbout()
	{
		about.show();
		about.toFront();
	}

	public static void closeAbout()
	{
		about.close();
	}

	public static void hideAddBill()
	{
		Main.addBill.hide();
	}

	public static void closeLogin()
	{
		LoginStage.close();
	}

	public static void closeAll()
	{

		if (Main.output != null)
		{
			try
			{
				if (!LoginController.props.getProperty("Backup_URL").equals("") || LoginController.props.getProperty("Backup_URL") != null)
				{
					FullCalendarView.saveBackup(LoginController.props.getProperty("Backup_URL"));
				}

				Main.output.close();
				Main.out.close();
				Main.in.close();
				Main.input.close();

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		LoginStage.close();
		primaryStage.close();
		addBill.close();
		about.close();
		settings.close();
		Platform.exit();
	}

	public static ArrayList<Bill> stringToArrayList(String array)
	{
		ArrayList<Bill> allBills = new ArrayList<Bill>();
		ArrayList<String> allwords = new ArrayList<String>();

		allwords.addAll(Arrays.asList(array.split("\\|\\|\\|")));

		for (String bill : allwords)
		{
			Bill billout = new Bill("", "", "", "", "", "", "");
			billout.fromString(bill);
			allBills.add(billout);
		}

		return allBills;
	}

	public static String arraylistToString(ArrayList<Bill> arrayList)
	{
		ArrayList<String> outputlist = new ArrayList<String>();

		for (Bill bill : arrayList)
		{
			outputlist.add(bill.toString());
		}

		String output = String.join("|||", outputlist);

		return output;
	}

	public static Boolean getBool(String bool)
	{
		if (bool.equals("true"))
		{
			return true;
		}
		else if (bool.equals("false"))
		{
			return false;
		}

		return false;

	}
}