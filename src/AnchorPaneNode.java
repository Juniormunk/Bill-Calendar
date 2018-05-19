
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane
{
	private LocalDate date;
	private ArrayList<Bill> data = new ArrayList<Bill>();

	/**
	 * Create a anchor pane node. Date is not assigned in the constructor.
	 * 
	 * @param children
	 *            children of the anchor pane
	 */
	public AnchorPaneNode(Node... children)
	{
		super(children);
		this.setOnMouseClicked(e ->
		{
			select();
		});
	}

	public void select()
	{
		Globals.selected = this;
		FullCalendarView.resetGridColor();
		this.setBackground(new Background(new BackgroundFill(LoginController.getColor("Select_Color"), CornerRadii.EMPTY, Insets.EMPTY)));
		FullCalendarView.updateSelected();
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public ArrayList<Bill> getData()
	{
		return data;
	}

	public void addData(Bill data)
	{
		this.data.add(data);
	}

	public void clearData()
	{
		data.removeAll(data);
	}

	public void loadData(ArrayList<Bill> data)
	{
		this.data = data;
	}
}
