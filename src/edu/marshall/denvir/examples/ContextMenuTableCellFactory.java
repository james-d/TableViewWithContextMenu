package edu.marshall.denvir.examples;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ContextMenuTableCellFactory<S, T> implements
		Callback<TableColumn<S, T>, TableCell<S, T>> {
	
	private final Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory ;
	private final Callback<TableCell<S, T>, List<MenuItem>> menuItemFactory ;
	
	public ContextMenuTableCellFactory(Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory, Callback<TableCell<S,T>, List<MenuItem>> menuItemFactory) {
		this.cellFactory = cellFactory ;
		this.menuItemFactory = menuItemFactory ;
	}
	
	public ContextMenuTableCellFactory(Callback<TableCell<S,T>, List<MenuItem>> menuItemFactory) {
		this(null, menuItemFactory);
	}

	@Override
	public TableCell<S, T> call(TableColumn<S, T> tableColumn) {
		final TableCell<S, T> cell ;
		if (cellFactory == null) {
			cell = new TableCell<S, T>(){
				@Override
				public void updateItem(T item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item.toString());
					}
				}
			};
		} else {
			cell = cellFactory.call(tableColumn);
		}
		cell.itemProperty().addListener(new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> observable,
					T oldValue, T newValue) {
				if (newValue == null) {
					cell.setContextMenu(null);
				} else {
					cell.setContextMenu(createContextMenu(cell));
				}
			}
			
		});

		return cell;
	}
	
	private ContextMenu createContextMenu(TableCell<S, T> cell) {
		ContextMenu menu = new ContextMenu();
		TableRow<?> row = cell.getTableRow() ;
		if (row != null) {
			ContextMenu rowMenu = row.getContextMenu() ;
			if (rowMenu == null) {
				TableView<S> table = cell.getTableView() ;
				ContextMenu tableMenu = table.getContextMenu() ;
				if (tableMenu != null) {
					menu.getItems().addAll(tableMenu.getItems());
					menu.getItems().add(new SeparatorMenuItem());
				}
			} else {
				menu.getItems().addAll(rowMenu.getItems());
				menu.getItems().add(new SeparatorMenuItem());
			}
		}
		menu.getItems().addAll(menuItemFactory.call(cell));
		return menu ;
	}

}
