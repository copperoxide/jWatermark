/*
Copyright (c) 2011, Carlos Tse <copperoxide@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package jwatermark;

import static jwatermark.Constant.*;
import static jwatermark.Util.*;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ImageWatermarkGui {

	private static final byte TYPE_SRC = 1, TYPE_WATERMARK = 2;

	private static final Point SHELL_SIZE = new Point(550, 180);

	public ImageWatermarkGui(){
		display = new Display();
		shell = new Shell(display);
		icon = new Image(display, "icon.png");
		initInterface();
	}

	private Display display;
	private Shell shell;
	private Text txtSrc, txtWatermark;
	private Image icon;
	private Combo comboType;

	public void run(){
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();

			shell.isDisposed();
		}
	}

	private void initInterface(){
		int i = 0;
		shell.setSize(SHELL_SIZE);
		shell.setMinimumSize(SHELL_SIZE);
		shell.setText(TXT[i++] + " " + TXT[i++]);
		shell.setImage(icon);

		GridData gridData = new GridData();
 		gridData.horizontalAlignment = GridData.FILL;
 		gridData.grabExcessHorizontalSpace = true;
// 		gridData.grabExcessVerticalSpace = true;

		// menu bar
	    shell.setMenuBar(getMenuBar());

	    // layout
	    shell.setLayout(new GridLayout(3, false));

	    // 1st row
	    final Label lbl1 = new Label(shell, SWT.NONE);
	    lbl1.setText(TXT[i++]);

	    txtSrc = new Text(shell, SWT.NONE);
	    txtSrc.setLayoutData(gridData);

	    final Button btn1 = new Button(shell, SWT.PUSH);
	    btn1.addSelectionListener(new Open(TYPE_SRC));
	    btn1.setText(TXT[i++]);

	    // 2nd row
	    final Label lbl2 = new Label(shell, SWT.NONE);
	    lbl2.setText(TXT[i++]);

	    txtWatermark = new Text(shell, SWT.NONE);
	    txtWatermark.setLayoutData(gridData);

	    final Button btn2 = new Button(shell, SWT.PUSH);
	    btn2.addSelectionListener(new Open(TYPE_WATERMARK));
	    btn2.setText(TXT[i++]);

	    // 3rd row
	    final Label lbl3 =new Label(shell, SWT.NONE);
	    lbl3.setText(TXT[i++]);

	    comboType = new Combo(shell, SWT.DROP_DOWN);
	    comboType.setItems(MARK_ITEMS);
	    comboType.select(MARK_ITEMS.length - 1); // default repeat

	    new Label(shell, SWT.NONE);

	    // 4th row
	    new Label(shell, SWT.NONE);

	    final Button btn3 = new Button(shell, SWT.PUSH);
	    btn3.addSelectionListener(new Save());
	    btn3.setText(TXT[i++]);

	    log("shell open");
	    shell.open();
	}

	private Menu getMenuBar(){
		int i = 20;
		final Menu 	menuBar = new Menu(shell, SWT.BAR),
					fileMenu = new Menu(shell, SWT.DROP_DOWN),
					helpMenu = new Menu(shell, SWT.DROP_DOWN);

		final MenuItem file = new MenuItem(menuBar, SWT.CASCADE);
	    file.setText(TXT[i++]);
	    file.setMenu(fileMenu);

	    final MenuItem openSrcItem = new MenuItem(fileMenu, SWT.PUSH);
	    openSrcItem.setText(TXT[i++]);
	    openSrcItem.setAccelerator(SWT.CTRL + 'O');

	    final MenuItem openWatermarkItem = new MenuItem(fileMenu, SWT.PUSH);
	    openWatermarkItem.setText(TXT[i++]);
	    openWatermarkItem.setAccelerator(SWT.CTRL + 'W');

	    new MenuItem(fileMenu, SWT.SEPARATOR);

	    final MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
	    saveItem.setText(TXT[i++]);
	    saveItem.setAccelerator(SWT.CTRL + 'S');

	    new MenuItem(fileMenu, SWT.SEPARATOR);

	    final MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	    exitItem.setText(TXT[i++]);
	    exitItem.setAccelerator(SWT.CTRL + 'Q');

	    openSrcItem.addSelectionListener(new Open(TYPE_SRC));
	    openWatermarkItem.addSelectionListener(new Open(TYPE_WATERMARK));
	    saveItem.addSelectionListener(new Save());
	    exitItem.addSelectionListener(new Exit());

	    final MenuItem help = new MenuItem(menuBar, SWT.CASCADE);
	    help.setText(TXT[i++]);
	    help.setMenu(helpMenu);

	    final MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
	    aboutItem.setText(TXT[i++]);
	    aboutItem.addSelectionListener(new About());

	    return menuBar;
	}

	class About implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {}
		public void widgetSelected(SelectionEvent e) {
			AboutDialog aboutDialog = new AboutDialog(display, icon, TXT);
			aboutDialog.show();
		}
	}

	class Open implements SelectionListener {
		public Open(byte type){
			this.type = type;
		}
		private byte type;
		public void widgetDefaultSelected(SelectionEvent e) {}
		public void widgetSelected(SelectionEvent e) {
			log("Open");
			FileDialog fd = new FileDialog (shell, SWT.OPEN);
			fd.setText("Open");
			fd.setFilterPath("C:/");

			switch (type){
			case TYPE_SRC:
				fd.setFilterExtensions(new String[]{ "*.jpg", "*.gif", "*.png", "*.*" });

				try {
					String selected = fd.open();
					log("src: " + selected);
					txtSrc.setText(selected);

				} catch (Exception ex){
					log("src, Exception: " + ex.getMessage());
				}
				break;

			case TYPE_WATERMARK:
				fd.setFilterExtensions(new String[]{ "*.png", "*.gif", "*.*" });

				try {
					String selected = fd.open();
					log("watermark: " + selected);
					txtWatermark.setText(selected);

				} catch (Exception ex){
					log("watermark, Exception: " + ex.getMessage());
				}
				break;

			default:
				log("unknow type: " + type);
			}
		}
	}

	class Save implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {}
		public void widgetSelected(SelectionEvent e) {
			byte type = MARK_MAP.get(comboType.getText());
			log("Save, type: " + type);

			File[] files = new File[]{
					isMissing(txtSrc.getText())? null: new File(txtSrc.getText()),
					isMissing(txtWatermark.getText())? null: new File(txtWatermark.getText())};

			boolean b[] = new boolean[]{ ImageWatermark.isReadableFile(files[0]), ImageWatermark.isReadableFile(files[1]) };
			log("source: " + b[0] + ", watermark: " + b[1]);

			if (!b[0] || !b[1]){
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				messageBox.setMessage(TXT[ER_MSG_ST] + (!b[0]? TXT[ER_MSG_ST + 1]: TXT[ER_MSG_ST + 2]) + TXT[ER_MSG_ST + 3]);
				messageBox.open();
				return;
			}

			FileDialog fd = new FileDialog (shell, SWT.SAVE);
			fd.setText("Save");
			fd.setFilterPath("C:/");
			fd.setFilterExtensions(new String[]{ "*.jpg" });

			try {
				String selected = fd.open();
				boolean success = false;

				switch(type){
				case MARK_LEFT_TOP:
				case MARK_RIGHT_TOP:
				case MARK_CENTER:
				case MARK_LEFT_BOTTOM:
				case MARK_RIGHT_BOTTOM:
					success = ImageWatermark.markImage(files[0], files[1], new File(selected), type);
					break;

				case MARK_REPEAT:
					success = ImageWatermark.markWholeImage(files[0], files[1], new File(selected));
					break;
				}

				log("Saved -> " + selected + ", success: " + success);
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setMessage(success? TXT[7]: TXT[8]);
				messageBox.open();

			} catch (Exception ex){
				log("Saved, Exception: " + ex.getMessage());
			}
		}
	}

	class Exit implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {}
		public void widgetSelected(SelectionEvent e) {
			log("Exit");
			shell.dispose();
		}
	}

}
