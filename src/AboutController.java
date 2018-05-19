
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AboutController
{

	@FXML
	private Button close;

	@FXML
	void closeWindow(MouseEvent event)
	{
		Main.closeAbout();
	}

}
