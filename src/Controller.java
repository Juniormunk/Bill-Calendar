
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.Optional;

import org.controlsfx.control.ToggleSwitch;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Controller
{
	@FXML
	Label dateLabel;
	@FXML
	Pane calendarPane;
	@SuppressWarnings("rawtypes")
	@FXML
	ListView listView;
	@FXML
	Button addButton;
	@FXML
	Hyperlink HL;

	@FXML
	Label dateInfo;

	@FXML
	Label amountInfo;

	@FXML
	Label companyInfo;

	@FXML
	Label nameInfo;

	@FXML
	TextArea otherInfo;

	@FXML
	Button deleteButton;

	@FXML
	ToggleSwitch Paid;

	@FXML
	Button editButton;

	@FXML
	void selectBill(MouseEvent event)
	{

		if (listView.getSelectionModel().getSelectedIndex() >= 0 && listView.getSelectionModel().getSelectedIndex() <= Globals.selected.getData().size())
		{
			if (event.getClickCount() >= 2 && Globals.selectedBill == Globals.selected.getData().get(listView.getSelectionModel().getSelectedIndex()))
			{
				if (Globals.selectedBill.name.contains(" (PAID)"))
				{

					Globals.selectedBill.name = Globals.selectedBill.name.substring(0, Globals.selectedBill.name.length() - 7);
				}
				Globals.addBillcontroller.isEdit = true;
				Globals.addBillcontroller.dateField.setValue(Globals.selected.getDate());
				Globals.addBillcontroller.nameField.setText(Globals.selectedBill.name);
				Globals.addBillcontroller.otherField.setText(Globals.selectedBill.info);
				Globals.addBillcontroller.linkField.setText(Globals.selectedBill.link);
				Globals.addBillcontroller.comField.setText(Globals.selectedBill.companyName);
				Globals.addBillcontroller.amountField.setText(Globals.selectedBill.amountDue);
				Globals.addBillcontroller.paidToggle.setSelected(Main.getBool(Globals.selectedBill.paid));
				Main.showAddBill();

			}
			Globals.selectedBillIndex = listView.getSelectionModel().getSelectedIndex();

			Globals.selectedBill = Globals.selected.getData().get(listView.getSelectionModel().getSelectedIndex());
			this.HL.setText(Globals.selectedBill.link);
			this.nameInfo.setText(Globals.selectedBill.name);
			this.companyInfo.setText(Globals.selectedBill.companyName);
			this.dateInfo.setText(Globals.selectedBill.date);
			this.amountInfo.setText(Globals.selectedBill.amountDue);
			this.otherInfo.setText(Globals.selectedBill.info);
			this.HL.setDisable(false);
			this.editButton.setDisable(false);
			this.deleteButton.setDisable(false);
			this.Paid.setDisable(false);
			Globals.con.Paid.setSelected(Main.getBool(Globals.selectedBill.paid));

		}

	}

	@FXML
	void OpenLink(ActionEvent event)
	{
		try
		{
			java.awt.Desktop.getDesktop().browse(new URI(Globals.selectedBill.link));
		}
		catch (IOException | URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void ChangePaid(MouseEvent event)
	{
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("You are changing the paid status of this bill would you like to continue?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent())
		{
			if (result.get() == ButtonType.OK)
			{
				System.out.println("CHANGE");
				Globals.selectedBill.paid = Paid.selectedProperty().getValue().toString();
				FullCalendarView.addProp(Globals.selected.getDate().toString(), Main.arraylistToString(Globals.selected.getData()));
				FullCalendarView.populateCalendar(Globals.currentYearMonth);
			}
			else
				Paid.setSelected(Main.getBool(Globals.selectedBill.paid));
		}
		else
		{
			System.out.println("ERROR");
			Paid.setSelected(Main.getBool(Globals.selectedBill.paid));
		}

	}

	@FXML
	void Delete(KeyEvent event)
	{
		if (event.getCode().equals(KeyCode.DELETE))
		{
			if (Globals.selected.getData().size() >= listView.getSelectionModel().getSelectedIndex() && listView.getSelectionModel().getSelectedIndex() >= 0)
			{
				Globals.selected.getData().remove(Globals.selected.getData().get(listView.getSelectionModel().getSelectedIndex()));

				if (Globals.selected.getData().size() > 0)
				{
					FullCalendarView.addProp(Globals.selected.getDate().toString(), Main.arraylistToString(Globals.selected.getData()));

					FullCalendarView.updateData();
				}
				else
				{
					FullCalendarView.removeProp(Globals.selected.getDate().toString());
					FullCalendarView.updateData();
				}
			}

		}
	}

	@FXML
	void deleteButtonAction(MouseEvent event)
	{

		if (Globals.selected.getData().size() >= listView.getSelectionModel().getSelectedIndex() && listView.getSelectionModel().getSelectedIndex() >= 0)
		{
			Globals.selected.getData().remove(Globals.selected.getData().get(listView.getSelectionModel().getSelectedIndex()));

			if (Globals.selected.getData().size() > 0)
			{
				FullCalendarView.addProp(Globals.selected.getDate().toString(), Main.arraylistToString(Globals.selected.getData()));
				FullCalendarView.updateData();
			}
			else
			{
				FullCalendarView.removeProp(Globals.selected.getDate().toString());
				FullCalendarView.updateData();
			}
		}
	}

	@FXML
	void EditButton(MouseEvent event)
	{
		if (Globals.selectedBill.name.contains(" (PAID)"))
		{

			Globals.selectedBill.name = Globals.selectedBill.name.substring(0, Globals.selectedBill.name.length() - 7);

		}

		Globals.addBillcontroller.isEdit = true;
		Globals.addBillcontroller.dateField.setValue(Globals.selected.getDate());
		Globals.addBillcontroller.nameField.setText(Globals.selectedBill.name);
		Globals.addBillcontroller.otherField.setText(Globals.selectedBill.info);
		Globals.addBillcontroller.linkField.setText(Globals.selectedBill.link);
		Globals.addBillcontroller.comField.setText(Globals.selectedBill.companyName);
		Globals.addBillcontroller.amountField.setText(Globals.selectedBill.amountDue);
		Globals.addBillcontroller.paidToggle.setSelected(Main.getBool(Globals.selectedBill.paid));
		Main.addBill.show();
	}

}
