import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;

import org.controlsfx.control.ToggleSwitch;

public class settingsController
{

	@FXML
	ToggleSwitch useNotifs;

	@FXML
	ColorPicker todayColor;

	@FXML
	Button browseButton;

	@FXML
	TextField urlBar;

	@FXML
	ColorPicker selectColor;

	@FXML
	Button applyButton;

	@FXML
	Button cancelButton;

	@FXML
	void applySettings(MouseEvent event)
	{
		FullCalendarView.addProp("Today_Color", todayColor.getValue().toString());
		FullCalendarView.addProp("Select_Color", selectColor.getValue().toString());
		FullCalendarView.addProp("Use_Noti", useNotifs.selectedProperty().getValue().toString());
		FullCalendarView.addProp("Backup_URL", urlBar.getText());

		Main.settings.hide();
		Globals.selected.select();
		FullCalendarView.highlightToday();
	}

	@FXML
	void closeSettings(MouseEvent event)
	{
		Main.settings.hide();
	}

	@FXML
	void browseFiles(MouseEvent event)
	{
		DirectoryChooser chooser = new DirectoryChooser();
		File selectedDirectory = chooser.showDialog(Main.settings);
		if (selectedDirectory != null)
		{
			urlBar.setText(selectedDirectory.getAbsolutePath());
		}
	}
}
