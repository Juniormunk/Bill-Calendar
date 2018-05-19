
import java.util.ArrayList;
import java.util.Arrays;

public class Bill
{

	public String name = new String("");
	public String companyName = new String("");
	public String date = new String("");
	public String link = new String("");
	public String amountDue = new String("");
	public String info = new String("");
	public String paid = new String("");
	public String extra1 = new String("");
	public String extra2 = new String("");
	public String extra3 = new String("");
	public String extra4 = new String("");
	public String extra5 = new String("");
	public String extra6 = new String("");
	public String extra7 = new String("");
	public String extra8 = new String("");
	public String extra9 = new String("");
	public String extra10 = new String("");
	// The extras are just in case I would like to add other data later.

	public Bill(String name, String companyName, String date, String link, String amountDue, String info, String paid)
	{
		super();
		this.name = name;
		this.companyName = companyName;
		this.date = date;
		this.link = link;
		this.amountDue = amountDue;
		this.info = info;
		this.paid = paid;
		if (!name.contains("(PAID)"))
		{
			if (Main.getBool(paid))
			{
				name = name.concat(" (PAID)");
			}

		}
		else if (name.contains("(PAID)"))
		{
			if (!Main.getBool(paid))
			{
				name = name.substring(0, name.length() - 7);
			}
		}
	}

	@Override
	public String toString()
	{
		if (name.equals(""))
		{
			name = " ";
		}
		if (companyName.equals(""))
		{
			companyName = " ";
		}
		if (date.equals(""))
		{
			date = " ";
		}

		if (link.equals(""))
		{
			link = " ";
		}
		if (amountDue.equals(""))
		{
			amountDue = " ";
		}
		if (info.equals(""))
		{
			info = " ";
		}
		if (paid.equals(""))
		{
			paid = " ";
		}
		if (extra1.equals(""))
		{
			extra1 = " ";
		}
		if (extra2.equals(""))
		{
			extra2 = " ";
		}
		if (extra3.equals(""))
		{
			extra3 = " ";
		}
		if (extra4.equals(""))
		{
			extra4 = " ";
		}
		if (extra5.equals(""))
		{
			extra5 = " ";
		}
		if (extra6.equals(""))
		{
			extra6 = " ";
		}
		if (extra7.equals(""))
		{
			extra7 = " ";
		}
		if (extra8.equals(""))
		{
			extra8 = " ";
		}
		if (extra9.equals(""))
		{
			extra9 = " ";
		}
		if (extra10.equals(""))
		{
			extra10 = " ";
		}
		if (!name.contains(" (PAID)"))
		{
			if (Main.getBool(paid))
			{
				name = name.concat(" (PAID)");
			}
		}
		else if (name.contains(" (PAID)"))
		{
			if (!Main.getBool(paid))
			{
				name = name.substring(0, name.length() - 7);
			}
		}

		return "Bill [link=" + link + "|| name=" + name + "|| companyName=" + companyName + "|| date=" + date + "|| info=" + info + "|| amountDue=" + amountDue + "|| paid=" + paid + "|| extra1=" + extra1 + "|| extra2=" + extra2
				+ "|| extra3=" + extra3 + "|| extra4=" + extra4 + "|| extra5=" + extra5 + "|| extra6=" + extra6 + "|| extra7=" + extra7 + "|| extra8=" + extra8 + "|| extra9=" + extra9 + "|| extra10=" + extra10 + "]";
	}

	public void fromString(String data)
	{
		data = data.substring(6, data.length() - 1);
		ArrayList<String> arr = new ArrayList<String>();
		arr.addAll(Arrays.asList(data.split("\\|\\|")));

		link = arr.get(0).split("=")[1];
		name = arr.get(1).split("=")[1];
		companyName = arr.get(2).split("=")[1];
		date = arr.get(3).split("=")[1];
		info = arr.get(4).split("=")[1];
		amountDue = arr.get(5).split("=")[1];
		paid = arr.get(6).split("=")[1];
		if (!name.contains(" (PAID)"))
		{
			if (Main.getBool(paid))
			{
				name = name.concat(" (PAID)");
			}
		}
		else if (name.contains(" (PAID)"))
		{
			if (!Main.getBool(paid))
			{
				name = name.substring(0, name.length() - 7);
			}
		}

	}

}
