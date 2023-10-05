package com.example.My_Database.Services;


import com.example.My_Database.Domain.Entity.Table;
import com.example.My_Database.Domain.Entity.Row;
import com.example.My_Database.Domain.Entity.types.Attribute;
import com.example.My_Database.Domain.Entity.types.Types;
import com.example.My_Database.Domain.Entity.types.Value;
import com.example.My_Database.GUIForms.TableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.*;
import java.util.List;


public class TableHandler {
    public TableModel table;
    private int numOfColumns;
    ArrayList<String[]> dataArrayList = new ArrayList<>();
    String[] columns;
    String[] emptyRow;

    public TableHandler(Table t) {
        table = new TableModel();
        updateColumns(t);
        initializeTable(t);
    }

    public void initializeTable(Table t) {
        table.setData(getData(t));
    }

    public Boolean update(Table t) {
        Boolean result = true;
        for (int rowInd : table.changedRows) {
            int colInd = 0;
            for (Attribute attr : t.listColumns()) {
                Types type = attr.getType();
                String newVal = table.getValueAt(rowInd, colInd);
                Attribute newAttr = Attribute.getAttribute(attr.getName(), type, newVal);
                if (newAttr.validate(newVal)) {
                    Value val = t.getRows().get(rowInd).getValueHashMap().get(attr.getName());
                    val.setVal(newVal);
                    t.getRows().get(rowInd).getValueHashMap().put(attr.getName(), val);
                } else {
                    result = false;
                }
                colInd++;
            }
        }
        return result;
    }

    private String[][] getData(Table t) {
        String[][] data = new String[t.getRows().size()][t.listColumns().size()];
        for (int i = 0; i < t.getRows().size(); i++) {
            String[] row = new String[t.listColumns().size()];
            int j = 0;
            HashMap<String, Value> values = t.getRows().get(i).getValueHashMap();
            for(Attribute attr: t.listColumns()){
                row[j++] = values.get(attr.name).toString();
            }
            data[i] = row;
        }
        return data;
    }

    private void updateColumns(Table t) {
        Integer attrSize = t.listColumns().size();
        String[] colNames = new String[attrSize];
        columns = new String[attrSize];
        emptyRow = new String[attrSize];
        int i = 0;
        for (Attribute attr : t.listColumns()) {
            columns[i] = attr.getName();
            colNames[i] = attr.getName() + "(" + attr.getType().name() + ")";
            emptyRow[i++] = attr.getDefault().toString();
        }
        table.setColumnNames(colNames);
    }


    public void initColumnSizes(JTable table) {
        TableModel model = (TableModel) table.getModel();
        TableColumn column;
        Component comp;
        int headerWidth;
        int cellWidth;
        TableCellRenderer headerRenderer =
              table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < numOfColumns; i++) {
            column = table.getColumnModel().getColumn(i);
            comp = headerRenderer.getTableCellRendererComponent(
                  null, column.getHeaderValue(),
                  false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                  getTableCellRendererComponent(
                        table, columns[i],
                        false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    public void insertRow() {
        String[][] newData = new String[dataArrayList.size() + 1][numOfColumns];
        dataArrayList.add(emptyRow);
        for (int i = 0; i < dataArrayList.size(); i++) {
            newData[i] = dataArrayList.get(i);
        }
        table.setData(newData);
    }

    public void deleteRow(int ind) {

        String[][] newData = new String[dataArrayList.size() - 1][numOfColumns];
        for (int i = 0; i < ind; i++) {
            newData[i] = dataArrayList.get(i);
        }
        for (int i = ind + 1; i < dataArrayList.size(); i++) {
            newData[i - 1] = dataArrayList.get(i);
        }
        table.setData(newData);
    }



    public void removeDuplicates(Table table) {
        List<Row> rows = table.getRows();
        table.deleteDuplicateRows(rows);
    }


}

