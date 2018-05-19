
import java.util.ArrayList;

import org.controlsfx.control.ToggleSwitch;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NewBillController
{

	@FXML
	Button applyButton;

	@FXML
	TextArea otherField;

	@FXML
	TextField nameField;

	@FXML
	DatePicker dateField;

	@FXML
	TextField linkField;

	@FXML
	TextField comField;

	@FXML
	TextField amountField;

	@FXML
	ToggleSwitch paidToggle;

	boolean isEdit = false;

	@FXML
	void createBill(MouseEvent event)
	{
		ArrayList<Bill> list = new ArrayList<Bill>();
		if (Globals.props.getProperty(this.dateField.getValue().toString()) != null)
		{
			list.addAll(Main.stringToArrayList(Globals.props.getProperty(this.dateField.getValue().toString())));
		}
		if (!isEdit || !this.dateField.getValue().toString().equals(Globals.selected.getDate().toString()))
		{
			list.add(new Bill(nameField.getText(), comField.getText(), dateField.getValue().toString(), linkField.getText(), amountField.getText(), otherField.getText(), paidToggle.selectedProperty().getValue().toString()));
		}
		else
		{
			if (!this.dateField.getValue().toString().equals(Globals.selected.getDate().toString()))
			{
				list.add(new Bill(nameField.getText(), comField.getText(), dateField.getValue().toString(), linkField.getText(), amountField.getText(), otherField.getText(), paidToggle.selectedProperty().getValue().toString()));

			}
			else
			{
				list.set(Globals.con.listView.getSelectionModel().getSelectedIndex(),
						new Bill(nameField.getText(), comField.getText(), dateField.getValue().toString(), linkField.getText(), amountField.getText(), otherField.getText(), paidToggle.selectedProperty().getValue().toString()));

			}
		}
		FullCalendarView.addProp(this.dateField.getValue().toString(), Main.arraylistToString(list));
		Main.hideAddBill();
		this.nameField.setText("");
		this.otherField.setText("");
		this.linkField.setText("");
		this.comField.setText("");
		this.amountField.setText("");
		FullCalendarView.populateCalendar(Globals.currentYearMonth);
		isEdit = false;
	}

}