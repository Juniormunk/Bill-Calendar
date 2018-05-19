import java.io.FileInputStream;
import java.io.OutputStream;
import java.time.YearMonth;
import java.util.Properties;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class Globals
{
	public static YearMonth currentYearMonth;
	public static Controller con;
	public static AnchorPaneNode today;
	public static Bill selectedBill;
	public static int selectedBillIndex;
	public static AnchorPaneNode selected;
	public static Properties props;
	
	
	public static OutputStream output = null;
	public static FileInputStream input = null;

	public static CipherOutputStream out;
	public static CipherInputStream in;

	public static NewBillController addBillcontroller;
	public static settingsController settingsController;
	public static LoginController loginController;
}
