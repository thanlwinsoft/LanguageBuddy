package org.thanlwinsoft.languagetest.eclipse.print;


import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.thanlwinsoft.languagetest.language.test.UniversalLanguage;
import org.thanlwinsoft.schemas.languagetest.module.LangType;
import org.thanlwinsoft.schemas.languagetest.module.TestItemType;

import com.ibm.icu.text.DateFormat;

public class PrintTestItems 
{
	PrinterData printData = null;
	Printer printer = null;
	TestItemType [] items = null;
	boolean printImages = false;
	LangType [] langs = null;
	final static float MARGIN_X = 1.0f;//inches
	final static float MARGIN_Y = 1.0f;//inches
	final static float COL_BORDER = 0.5f;
	final static float ROW_SPACE = 0.1f;
	
	//final int NATIVE = 0;
	//final int FOREIGN = 1;
	final int IMAGE = 2;
	final int COLUMNS = 3;
	
	String title = "";
	Rectangle pageArea = null;
	int yOffset = 0;
	int rowSpace = 0;
	int [] widths = null;
	int [] xOffsets = null;
	Font [] fonts = null;
	TextLayout []layout = null;
	TextLayout footerLeft;
	TextLayout footerCentre;
	TextLayout footerRight;
	GC gc = null;
	boolean printPage = false;
	
	public PrintTestItems(PrinterData pd, String title, TestItemType [] items, LangType leftLang, LangType rightLang)
	{
		this.title = title;
		this.printData = pd;
		this.printer = new Printer(pd);
		this.items = items;
		this.langs = new LangType[] { leftLang, rightLang };
	}
	
	public boolean doPrint(IProgressMonitor monitor)
	{
		boolean ok = true;
		
		Point dpi = printer.getDPI();
		Rectangle physicalArea = printer.getBounds();
		Rectangle printArea = printer.getClientArea();
		int xMargin = (int)(dpi.x * MARGIN_X);
		int yMargin = (int)(dpi.y * MARGIN_Y);
		footerLeft = new TextLayout(printer);
		footerLeft.setAlignment(SWT.LEFT);
		footerLeft.setFont(printer.getSystemFont());
		footerLeft.setText(title);
		footerRight = new TextLayout(printer);
		footerRight.setAlignment(SWT.RIGHT);
		footerRight.setFont(printer.getSystemFont());
		footerCentre = new TextLayout(printer);
		footerCentre.setAlignment(SWT.CENTER);
		footerCentre.setFont(printer.getSystemFont());
		String today = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
		footerCentre.setText(today);
		int minX = Math.max(printArea.x, xMargin);
		int minY = Math.max(printArea.y, yMargin);
		int maxX = Math.min(printArea.x + printArea.width, physicalArea.width - xMargin);
		int maxY = Math.min(printArea.y + printArea.height, physicalArea.height - yMargin);
		int footerHeight = footerLeft.getAscent() + footerLeft.getDescent();
		pageArea = new Rectangle(minX, minY, maxX - minX, maxY - minY - footerHeight);
		
		if (2 * footerLeft.getBounds().width + footerCentre.getBounds().width > 
			pageArea.width)
		{
			if (footerLeft.getBounds().width + footerCentre.getBounds().width <
				pageArea.width)
			{
				// add date after name
				footerCentre.setText("");
				footerLeft.setText(title + " " + today);
			}
			else
			{
				// drop the date
				footerCentre.setText("");
			}
		}
		
		
		final int colBorder = (int)(dpi.x * COL_BORDER);
		rowSpace = (int)(dpi.y * ROW_SPACE);
		yOffset = pageArea.y;
		int colWidth = (pageArea.width - colBorder)/ 2;
		if (printImages)
			colWidth = (pageArea.width - 2 * colBorder)/ 3;

		widths = new int [] { colWidth, colWidth, colWidth};
		xOffsets = new int [] { pageArea.x, pageArea.x + widths[0] + colBorder, 
				pageArea.x + widths[1] + widths[0] + 2 * colBorder};
		monitor.beginTask("Print test items", items.length);
		// setup the fonts
		FontData [] fd = new FontData[2];
		fonts = new Font[2];
		layout = new TextLayout[2];
		for (int i = 0; i < IMAGE; i++)
		{
			String fontName = langs[i].getFont();
			if (fontName == null) fontName = "Default";
			int fontSize = 12;
			int fontStyle = SWT.NORMAL;
			if (langs[i].isSetFontSize())
				fontSize = langs[0].getFontSize().intValue();
			if (langs[i].isSetFontStyle())
				fontStyle = langs[0].getFontStyle().intValue();
			fd[i] = new FontData(fontName, fontSize, fontStyle);
			fonts[i] = new Font(printer, fd[i]);
			layout[i] = new TextLayout(printer);
			layout[i].setFont(fonts[i]);
			layout[i].setWidth(widths[i]);
		}
		
		
		printer.startJob("Printing: " + title);
		gc = new GC(printer);
		gc.setLineStyle(SWT.LINE_SOLID);

		int page = 1;
		printPage = isPrintable(page);
		if (printPage)
			ok &= printer.startPage();
		yOffset = pageArea.y;
		drawHeader();
		
		// loop over the rows
		for (int row = 0; ok && row < items.length; row++)
		{
			int rowHeight = 0;
			for (int i = 0; i < COLUMNS; i++)
			{
				if (i == IMAGE)
				{
					
				}
				else
				{
					String text = "";
					for (int j = 0; j < items[i].sizeOfNativeLangArray(); j++)
					{
						if (items[row].getNativeLangArray(j).getLang().equals(langs[i].getLang()))
						{
							text = items[row].getNativeLangArray(j).getStringValue();
						}
					}
					for (int j = 0; j < items[i].sizeOfForeignLangArray(); j++)
					{
						if (items[row].getForeignLangArray(j).getLang().equals(langs[i].getLang()))
						{
							text = items[row].getForeignLangArray(j).getStringValue();
						}
					}
					layout[i].setText(text);
					Rectangle textBounds = layout[i].getBounds();
					rowHeight = Math.max(rowHeight, textBounds.height);
				}
			}
			// there isn't room, so draw it on the next page
			if ((yOffset + rowHeight + rowSpace) > (pageArea.y + pageArea.height))
			{
				if (printPage)
				{
					drawFooter(page);
					printer.endPage();
				}
				yOffset = pageArea.y;
				printPage = isPrintable(++page);
				if (printPage)
					ok &= printer.startPage();
				drawHeader();
			}
			
			if (printPage)
			{
				for (int i = 0; i < COLUMNS; i++)
				{
					if (i != IMAGE)
					{
						layout[i].draw(gc, xOffsets[i], yOffset);
					}
				}
				// draw a line underneath
				gc.setLineWidth(0);
				gc.drawLine(pageArea.x, yOffset + rowHeight + (rowSpace/2), 
						pageArea.x + pageArea.width, yOffset + rowHeight + (rowSpace/2));
			}
			yOffset += rowHeight + rowSpace;
			monitor.worked(1);
			if (monitor.isCanceled())
			{
				break;
			}
		}
		if (printPage)
		{
			drawFooter(page);
			printer.endPage();
		}
		
		printer.endJob();
		monitor.done();
		return true;
	}
	
	void drawHeader()
	{
		int rowHeight = 0;
		TextLayout headerLayout  = new TextLayout(printer);
		FontData defaultFD = printer.getSystemFont().getFontData()[0];
		FontData headerFontData = new FontData(defaultFD.getName(),
				defaultFD.getHeight(), SWT.BOLD);
		Font font = new Font(printer, headerFontData);
		headerLayout.setFont(font);
		
		for (int i = 0; i < COLUMNS; i++)
		{
			if (i == IMAGE)
			{
				
			}
			else
			{
				headerLayout.setWidth(widths[i]);
				UniversalLanguage ul = new UniversalLanguage(langs[i].getLang());
				ul = new UniversalLanguage(ul.getLanguageCode());
				headerLayout.setText(ul.getDescription());
				if (printPage)
					headerLayout.draw(gc, xOffsets[i], yOffset);
				rowHeight = Math.max(rowHeight, headerLayout.getBounds().height);
			}
		}
		yOffset += rowHeight + rowSpace;
		// draw a line underneath
		if (printPage)
		{
			gc.setLineWidth(2);
			gc.drawLine(pageArea.x, yOffset - (rowSpace/2), pageArea.x + pageArea.width, yOffset- (rowSpace/2));
		}
	}
	
	void drawFooter(int page)
	{
		footerLeft.draw(gc, pageArea.x, pageArea.y + pageArea.height);
		footerCentre.draw(gc, pageArea.x + (pageArea.width - footerCentre.getBounds().width)/2,
				pageArea.y + pageArea.height);
		footerRight.setText(Integer.toString(page));
		footerRight.draw(gc, pageArea.x + pageArea.width - footerRight.getBounds().width,
				pageArea.y + pageArea.height);
	}
	
	boolean isPrintable(int page)
	{
		if (printData.scope == PrinterData.ALL_PAGES)
			return true;
		if (printData.scope == PrinterData.PAGE_RANGE)
		{
			if (page >= printData.startPage && page <= printData.endPage)
				return true;
			return false;
		}
		// TBD selection
		return true;
	}
}
