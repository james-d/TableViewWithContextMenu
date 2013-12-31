package edu.marshall.denvir.examples;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ContextMenuTableRowFactory<T> implements Callback<TableView<T>, TableRow<T>> {
	
	private final Callback<T, List<MenuItem>> menuItemFactory ;
	private final Callback<TableView<T>, TableRow<T>> rowFactory ;
	
	public ContextMenuTableRowFactory(Callback<TableView<T>, TableRow<T>> rowFactory, Callback<T, List<MenuItem>> menuItemFactory) {
		this.rowFactory = rowFactory;
		this.menuItemFactory = menuItemFactory ;
	}
	
	public ContextMenuTableRowFactory(Callback<T, List<MenuItem>> menuItemFactory) {
		this(null, menuItemFactory);
	}
	

	@Override
	public TableRow<T> call(TableView<T> table) {
		final TableRow<T> row ;
		if (rowFactory == null) {
			row = new TableRow<T>();
		} else {
			row = rowFactory.call(table);
		}
		row.itemProperty().addListener(new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> observable,
					T oldValue, T newValue) {
				if (newValue == null) {
					row.setContextMenu(null);
				} else {
					row.setContextMenu(createContextMenu(row));
				}
			}
		});
		return row ;
	}

	private ContextMenu createContextMenu(final TableRow<T> row) {
		ContextMenu menu = new ContextMenu();
		ContextMenu tableMenu = row.getTableView().getContextMenu();
		if (tableMenu != null) {
			menu.getItems().addAll(tableMenu.getItems());
			menu.getItems().add(new SeparatorMenuItem());
		}
		menu.getItems().addAll(menuItemFactory.call(row.getItem()));
		return menu;
	}

}
