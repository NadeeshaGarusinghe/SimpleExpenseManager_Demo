package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by lenovo on 12/19/2019.
 */

public class PersistentTransactionDAO implements TransactionDAO {
    private DatabaseHelper databaseHelper;

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        databaseHelper.logTransaction(date,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return databaseHelper.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return databaseHelper.getPaginatedTransactionLogs(limit);
    }
}
