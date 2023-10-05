package com.example.My_Database;


import com.example.My_Database.Domain.Entity.Columns;
import com.example.My_Database.Domain.Entity.Row;
import com.example.My_Database.Domain.Entity.Table;
import com.example.My_Database.Domain.Entity.types.*;
import com.example.My_Database.utils.Result;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;


public class TableTest {
    private static Table table;
    private static Table expectedResultProjection;
    private static Table expectedResultAddAttribute;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addAttr_noDuplicate_tableWithDefaultColumn() {
        initialise();
        initialiseResultAddAttr();

        Result result = table.addAttr(Attribute.getAttribute("Mark", Types.REAL));

        String expected = new Gson().toJson(expectedResultAddAttribute);
        String actual = new Gson().toJson(table);
        assertEquals(expected, actual);
        assertTrue(result.isSuccessful());
    }

    @Test
    public void addAttr_duplicate_failed() {
        initialise();

        Result result = table.addAttr(Attribute.getAttribute("User", Types.STRING));


        String actual = new Gson().toJson(table);
        initialise();
        String expected = new Gson().toJson(table);
        assertEquals(expected, actual);
        assertFalse(result.isSuccessful());
    }

    @Test
    public void removeDuplicateRows_emptyTable() {
        Table emptyTable = new Table("EmptyTable");
        emptyTable.deleteDuplicateRows(emptyTable.getRows());
        assertTrue(emptyTable.getRows().isEmpty());
    }

    @Test
    public void removeDuplicateRows_noDuplicates() {
        initialise();

        int initialRowCount = table.getRows().size();
        table.deleteDuplicateRows(table.getRows());
        int rowCountAfterRemoval = table.getRows().size();


        assertEquals(initialRowCount, rowCountAfterRemoval);
    }

    @Test
    public void removeDuplicateRows_withDuplicates() {
        initialise_withD();

        int initialRowCount = table.getRows().size();
        table.deleteDuplicateRows(table.getRows());
        int rowCountAfterRemoval = table.getRows().size();

        assertTrue(rowCountAfterRemoval < initialRowCount);
    }

    @Test
    public void ValidateAttributes() {
        Attribute[] attributes = {
              new IntegerAttr("attr1"),
              new StringAttr("attr2"),
              new RealAttr("attr3"),
              new CharAttr("attr4"),
              new TextFileAttr("attr5"),
              new IntIntervalAttr("attr6")
        };
        System.out.println(attributes[5].getDefault());
        assertEquals(attributes[0].validate("10"), true);
        assertEquals(attributes[0].validate("10a"), false);
        assertEquals(attributes[1].validate("10blabla"), true);
        assertEquals(attributes[2].validate("10.0"), true);
        assertEquals(attributes[2].validate("10a"), false);
        assertEquals(attributes[3].validate("a"), true);
        assertEquals(attributes[3].validate("10a"), false);
        assertEquals(attributes[4].validate("C:\\Git\\LICENSE.txt"), true);
        assertEquals(attributes[4].validate("file"), false);
        assertEquals(attributes[5].validate("1, 3"), true);
        assertEquals(attributes[5].validate("3, 1"), false);
    }

    public void initialise() {
        table = new Table("Init");
        Attribute[] attributes = {
              Attribute.getAttribute("User", Types.STRING),
              Attribute.getAttribute("Age", Types.INTEGER),
              Attribute.getAttribute("Class", Types.CHAR)};
        HashMap<String, Value> row1 = new HashMap<>();
        row1.put("User", attributes[0].getValue("Kate"));
        row1.put("Age", attributes[1].getValue("15"));
        row1.put("Class", attributes[2].getValue("A"));

        HashMap<String, Value> row2 = new HashMap<>();
        row2.put("User", attributes[0].getValue("Kate"));
        row2.put("Age", attributes[1].getValue("15"));
        row2.put("Class", attributes[2].getValue("D"));

        HashMap<String, Value> row3 = new HashMap<>();
        row3.put("User", attributes[0].getValue("Kate"));
        row3.put("Age", attributes[1].getValue("16"));
        row3.put("Class", attributes[2].getValue("A"));
        for (Attribute attr : attributes) {
            table.addAttr(attr);
        }
        table.addRow(new Row(row1));
        table.addRow(new Row(row2));
        table.addRow(new Row(row3));
    }


    public void initialise_withD() {
        table = new Table("Init");
        Attribute[] attributes = {
                Attribute.getAttribute("User", Types.STRING),
                Attribute.getAttribute("Age", Types.INTEGER),
                Attribute.getAttribute("Class", Types.CHAR)};
        HashMap<String, Value> row1 = new HashMap<>();
        row1.put("User", attributes[0].getValue("Kate"));
        row1.put("Age", attributes[1].getValue("15"));
        row1.put("Class", attributes[2].getValue("A"));

        HashMap<String, Value> row2 = new HashMap<>();
        row2.put("User", attributes[0].getValue("Kate"));
        row2.put("Age", attributes[1].getValue("15"));
        row2.put("Class", attributes[2].getValue("A"));

        HashMap<String, Value> row3 = new HashMap<>();
        row3.put("User", attributes[0].getValue("Kate"));
        row3.put("Age", attributes[1].getValue("16"));
        row3.put("Class", attributes[2].getValue("A"));
        for (Attribute attr : attributes) {
            table.addAttr(attr);
        }
        table.addRow(new Row(row1));
        table.addRow(new Row(row2));
        table.addRow(new Row(row3));
    }
    public void initialiseResult() {
        expectedResultProjection = new Table("Projection");
        Attribute[] attributes = {
              Attribute.getAttribute("User",Types.STRING),
              Attribute.getAttribute("Age",Types.INTEGER),
        };
        HashMap<String, Value> row1 = new HashMap<>();
        row1.put("User", attributes[0].getValue("Kate"));
        row1.put("Age", attributes[1].getValue("15"));

        HashMap<String, Value> row3 = new HashMap<>();
        row3.put("User", attributes[0].getValue("Kate"));
        row3.put("Age", attributes[1].getValue("16"));

        for (Attribute attr : attributes) {
            expectedResultProjection.addAttr(attr);
        }
        expectedResultProjection.addRow(new Row(row1));
        expectedResultProjection.addRow(new Row(row3));
    }

    public void initialiseResultAddAttr() {
        expectedResultAddAttribute = new Table("Init");
        Attribute[] attributes = {
              Attribute.getAttribute("User", Types.STRING),
              Attribute.getAttribute("Age", Types.INTEGER),
              Attribute.getAttribute("Class", Types.CHAR),
              Attribute.getAttribute("Mark", Types.REAL)
        };
        HashMap<String, Value> row1 = new HashMap<>();
        row1.put("User", attributes[0].getValue("Kate"));
        row1.put("Age", attributes[1].getValue("15"));
        row1.put("Class", attributes[2].getValue("A"));
        row1.put("Mark", attributes[3].getDefault());

        HashMap<String, Value> row2 = new HashMap<>();
        row2.put("User", attributes[0].getValue("Kate"));
        row2.put("Age", attributes[1].getValue("15"));
        row2.put("Class", attributes[2].getValue("D"));
        row2.put("Mark", attributes[3].getDefault());

        HashMap<String, Value> row3 = new HashMap<>();
        row3.put("User", attributes[0].getValue("Kate"));
        row3.put("Age", attributes[1].getValue("16"));
        row3.put("Class", attributes[2].getValue("A"));
        row3.put("Mark", attributes[3].getDefault());
        for (Attribute attr : attributes) {
            expectedResultAddAttribute.addAttr(attr);
        }
        expectedResultAddAttribute.addRow(new Row(row1));
        expectedResultAddAttribute.addRow(new Row(row2));
        expectedResultAddAttribute.addRow(new Row(row3));
    }
}
