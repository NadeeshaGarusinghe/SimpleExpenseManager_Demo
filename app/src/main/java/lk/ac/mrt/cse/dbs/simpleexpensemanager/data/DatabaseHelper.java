package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by lenovo on 12/19/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query= "create table account( accountNo varchar(20) primary key, bankName varchar(50), accountHolderName varchar(50), balance double)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account");
        onCreate(sqLiteDatabase);
    }

    public void insertData(Account account){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        long result=db.insert("account",null,contentValues);
    }

    public void removeData(String accountNumber){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("account","accountNo="+accountNumber,null);
    }

    public Account getAccount(String accountNumber){
        String[] columns = {"accountNo","bankName","accountHolderName","balance"};
        String[] selectionArgs = {accountNumber};

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.query("account",columns,"accountNo =?",selectionArgs,null,null,null);
        String accountNo=cursor.getString(cursor.getColumnIndex("accountNo"));
        String bankName=cursor.getString(cursor.getColumnIndex("bankName"));
        String accountHolderName=cursor.getString(cursor.getColumnIndex("accountHolderName"));
        String balance=cursor.getString(cursor.getColumnIndex("balance"));

        return new Account(accountNo,bankName,accountHolderName,Double.parseDouble(balance));
    }

    public List<String> getAccountNumbersList(){

        List<String> accountList = new ArrayList<>();

        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns = {"accountNo"};

        Cursor cursor = db.query("account",columns,null,null,null,null,null);
        for (String result : cursor.getColumnNames()){
            accountList.add(result);
        }

        return accountList;
    }

    public List<Account> getAccountsList(){
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns = {"accountNo"};

        Cursor cursor = db.query("account",columns,null,null,null,null,null);
        for (String result : cursor.getColumnNames()){
            accountList.add(new Account(result,null,null,0));
        }
        return accountList;
    }


    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns = {"accountNo"};

        Cursor cursor = db.query("transaction",columns,null,null,null,null,null);
        for (String result : cursor.getColumnNames()){
            transactionList.add(new Transaction(null,result,null,0));
        }
        return transactionList;
    }


    public List<Transaction> getPaginatedTransactionLogs(int limit){
        List<Transaction> paginatedTransactionLogs = new ArrayList<>();

        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns = {"accountNo"};

        Cursor cursor = db.query("transaction",columns,null,null,null,null,null,Integer.toString(limit));
        for (String result : cursor.getColumnNames()){
            paginatedTransactionLogs.add(new Transaction(null,result,null,0));
        }
        return paginatedTransactionLogs;
    }
}
