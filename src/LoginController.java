
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LoginController
{
	// TODO: add support for color, add support for browse files, add support for cancel and accept buttons, add support for use notifications.

	@FXML
	private Pane window;

	@FXML
	private Rectangle utilbar;
	@FXML
	private Button login;

	@FXML
	private Button signup;

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label invalidLabel;

	private boolean dragable = true;

	private static final String folderPath = System.getProperty("user.home") + "\\documents\\BillCalandar";

	public static String fileName = "";

	static SecretKeySpec secretKeySpec;

	static String filePath;
	String keyPath;

	// Add a public no-args constructor

	public LoginController()
	{

	}

	@FXML

	private void initialize()
	{

		passwordField.setFocusTraversable(true);
		setInvalidLabel(false);
	}

	@FXML
	void login(ActionEvent event)
	{

		Platform.runLater(new Runnable()
		{

			@Override
			public void run()
			{
				String username = usernameField.getText();
				String password = passwordField.getText();

				fileName = UUID.nameUUIDFromBytes(username.toLowerCase().getBytes()) + ".properties";

				filePath = folderPath + "\\" + UUID.nameUUIDFromBytes(username.toLowerCase().getBytes()) + ".properties";

				if (!username.equals("") && !password.equals(""))
				{
					System.out.println(filePath);

					File directory = new File(folderPath);
					File propfilecheck = new File(filePath);

					if (directory.exists() || propfilecheck.exists())
					{
						setInvalidLabel(false);
						directory.mkdir();
						try
						{

							Main.input = new FileInputStream(filePath);

							String SALT2 = "Lets Not Talk About This";

							byte[] key = (SALT2 + username + password).getBytes("UTF-8");
							MessageDigest sha = MessageDigest.getInstance("SHA-1");
							key = sha.digest(key);
							key = Arrays.copyOf(key, 16); // use only first 128 bit

							SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

							LoginController.secretKeySpec = secretKeySpec;

							Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
							aes.init(Cipher.DECRYPT_MODE, secretKeySpec);

							Main.in = new CipherInputStream(Main.input, aes);
							props = new Properties();

							props.load(Main.in);

							Main.showPrimary();

						}
						catch (Exception io)
						{
							try
							{
								if (io.getMessage().contains("bad key"))
								{
									setInvalidLabelText("Incorrect Password Or Username");
									setInvalidLabel(true);
								}
								if (io.getMessage().contains("find"))
								{
									setInvalidLabelText("Incorrect Password Or Username");
									setInvalidLabel(true);
								}
							}
							catch (Exception io1)
							{
								io1.printStackTrace();
							}

						}
						finally
						{
							try
							{
								if (Main.input != null)
								{
									Main.input.close();
									Main.in.close();
								}
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}

						}
					}
					else
					{
						setInvalidLabelText("That Username Is Already Being Used");
						setInvalidLabel(true);
					}
				}
				else
				{
					setInvalidLabelText("Incorrect Password Or Username");
					setInvalidLabel(true);
				}

			}

		});
	}

	@FXML
	void signup(ActionEvent event)
	{
		Platform.runLater(new Runnable()
		{

			@Override
			public void run()
			{
				String username = usernameField.getText();
				String password = passwordField.getText();

				fileName = UUID.nameUUIDFromBytes(username.toLowerCase().getBytes()) + ".properties";

				filePath = folderPath + "\\" + UUID.nameUUIDFromBytes(username.toLowerCase().getBytes()) + ".properties";

				if (!username.equals("") && !password.equals(""))
				{

					File directory = new File(folderPath);
					File propfilecheck = new File(filePath);

					if (!directory.exists() || !propfilecheck.exists())
					{
						setInvalidLabel(false);
						directory.mkdir();
						try
						{
							Main.output = new FileOutputStream(filePath);

							String SALT2 = "Lets Not Talk About This";

							byte[] key = (SALT2 + username + password).getBytes("UTF-8");
							MessageDigest sha = MessageDigest.getInstance("SHA-1");
							key = sha.digest(key);
							key = Arrays.copyOf(key, 16); // use only first 128 bit

							SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

							LoginController.secretKeySpec = secretKeySpec;

							Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
							aes.init(Cipher.ENCRYPT_MODE, secretKeySpec);

							Main.out = new CipherOutputStream(Main.output, aes);

							props = new Properties();

							props.setProperty("Today_Color", "#f08585");
							props.setProperty("Select_Color", "#E0FFFF");
							props.setProperty("Use_Noti", "true");
							props.setProperty("Backup_URL", "");

							props.store(Main.out, null);

							Main.showPrimary();

						}

						catch (Exception io)
						{
							if (io.getMessage().contains("bad key"))
							{
								setInvalidLabelText("Incorrect Password Or Username");
								setInvalidLabel(true);
							}
							if (io.getMessage().contains("find"))
							{
								setInvalidLabelText("Incorrect Password Or Username");
								setInvalidLabel(true);
							}

						}
						finally
						{

							try
							{
								Main.out.close();
								Main.output.close();

							}
							catch (IOException e)
							{
								e.printStackTrace();
							}

						}
					}
					else
					{
						setInvalidLabelText("That Username Is Already Being Used");
						setInvalidLabel(true);
					}
				}

			}

		});

	}

	private static double xOffset = 0;
	private static double yOffset = 0;

	@FXML
	void startchangeWindowPos(MouseEvent event)
	{
		xOffset = event.getSceneX();
		yOffset = event.getSceneY();
	}

	@FXML
	void changeWindowPos(MouseEvent event)
	{
		if (dragable)
		{
			Main.moveWindow(event.getScreenX() - xOffset, event.getScreenY() - yOffset);
		}
	}

	@FXML
	void closeApp(MouseEvent event)
	{
		Main.close();
	}

	@FXML
	void stopDrag(MouseEvent event)
	{
		dragable = false;
	}

	@FXML
	void startDrag(MouseEvent event)
	{
		dragable = true;
	}

	public void setInvalidLabel(boolean show)
	{

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				FadeTransition fadeOut = new FadeTransition(Duration.millis(3000));

				fadeOut.setNode(invalidLabel);

				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.setCycleCount(1);
				fadeOut.setAutoReverse(false);

				if (show)
				{
					invalidLabel.setVisible(true);
					fadeOut.playFromStart();
				}
				else
				{
					invalidLabel.setVisible(false);
				}
			}
		});
	}

	public void setInvalidLabelText(String text)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				invalidLabel.setText(text);
			}
		});
	}

	public static Color getColor(String prop)
	{
		return Color.web(LoginController.props.getProperty(prop));
	}

	@FXML
	void loginKey(KeyEvent event)
	{

		if (event.getCode().equals(KeyCode.ENTER))
		{
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					String username = usernameField.getText();
					String password = passwordField.getText();

					fileName = UUID.nameUUIDFromBytes(username.toLowerCase().getBytes()) + ".properties";

					filePath = folderPath + "\\" + UUID.nameUUIDFromBytes(username.toLowerCase().getBytes()) + ".properties";

					if (!username.equals("") && !password.equals(""))
					{
						System.out.println(filePath);

						File directory = new File(folderPath);
						File propfilecheck = new File(filePath);

						if (directory.exists() || propfilecheck.exists())
						{
							setInvalidLabel(false);
							directory.mkdir();
							try
							{

								Main.input = new FileInputStream(filePath);

								String SALT2 = "Lets Not Talk About This";

								byte[] key = (SALT2 + username + password).getBytes("UTF-8");
								MessageDigest sha = MessageDigest.getInstance("SHA-1");
								key = sha.digest(key);
								key = Arrays.copyOf(key, 16); // use only first 128 bit

								SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

								LoginController.secretKeySpec = secretKeySpec;

								Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
								aes.init(Cipher.DECRYPT_MODE, secretKeySpec);

								Main.in = new CipherInputStream(Main.input, aes);
								props = new Properties();

								props.load(Main.in);

								Main.showPrimary();

							}
							catch (Exception io)
							{
								try
								{
									if (io.getMessage().contains("bad key"))
									{
										setInvalidLabelText("Incorrect Password Or Username");
										setInvalidLabel(true);
									}
									if (io.getMessage().contains("find"))
									{
										setInvalidLabelText("Incorrect Password Or Username");
										setInvalidLabel(true);
									}
								}
								catch (Exception io1)
								{
									io1.printStackTrace();
								}

							}
							finally
							{
								try
								{
									if (Main.input != null)
									{
										Main.input.close();
										Main.in.close();
									}
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}

							}
						}
						else
						{
							setInvalidLabelText("That Username Is Already Being Used");
							setInvalidLabel(true);
						}
					}
					else
					{
						setInvalidLabelText("Incorrect Password Or Username");
						setInvalidLabel(true);
					}

				}

			});
		}
	}
}
