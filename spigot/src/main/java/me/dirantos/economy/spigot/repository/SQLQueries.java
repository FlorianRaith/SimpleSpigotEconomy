package me.dirantos.economy.spigot.repository;

public final class SQLQueries {

    private static final String CREATE_BANKS_TABLE = "CREATE TABLE IF NOT EXISTS `%banksTable%` ( `uuid_most` BIGINT NOT NULL , `uuid_least` BIGINT NOT NULL , `wallet_balance` DECIMAL(19,2) NOT NULL, PRIMARY KEY (`uuid_most`, `uuid_least`) )";
    private static final String INSERT_BANK = "INSERT INTO `%banksTable%` (`uuid_most`, `uuid_least`, `wallet_balance`) VALUES (?,?,?)";
    private static final String SELECT_BANK = "SELECT `wallet_balance`, ( SELECT SUM(`balance`) FROM `%accountsTable%` WHERE `%accountsTable%`.`bank_uuid_most` = `uuid_most` AND `%accountsTable%`.`bank_uuid_least` = `uuid_least`) AS `bank_balance` FROM `%banksTable%` WHERE `uuid_most` = ? AND `uuid_least` = ?";
    private static final String UPDATE_BANK = "UPDATE `%banksTable%` SET `wallet_balance` = ? WHERE `uuid_most` = ? AND `uuid_least` = ?";
    private static final String DELETE_BANK = "DELETE FROM `%banksTable%` WHERE `uuid_most` = ? AND `uuid_least` = ?";

    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE IF NOT EXISTS `%accountsTable%` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `balance` DECIMAL(15,2) NOT NULL , `bank_uuid_most` BIGINT NOT NULL, `bank_uuid_least` BIGINT NOT NULL, PRIMARY KEY (`id`), FOREIGN KEY (`bank_uuid_most`, `bank_uuid_least`) REFERENCES `%banksTable%`(`uuid_most`, `uuid_least`) ON DELETE CASCADE )";
    private static final String INSERT_ACCOUNT = "INSERT INTO `%accountsTable%` (`balance`, `bank_uuid_most`, `bank_uuid_least`) VALUES (?,?,?)";
    private static final String SELECT_ACCOUNT = "SELECT * FROM `%accountsTable%` WHERE `id` = ?";
    private static final String SELECT_ACCOUNTS_BY_BANK = "SELECT * FROM `%accountsTable%` WHERE `bank_uuid_most` = ? AND `bank_uuid_least` = ?";
    private static final String UPDATE_ACCOUNT = "UPDATE `%accountsTable%` SET `balance` = ? WHERE `id` = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM `%accountsTable%` WHERE `id` = ?";

    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS `%transactionsTable%` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `account_id` INT UNSIGNED NOT NULL , `amount` DECIMAL(15,2) NOT NULL , `type` VARCHAR(16) NOT NULL , `date` DATE NOT NULL , `interest_rate` DOUBLE NULL , `sender_account_id` INT UNSIGNED NULL , `recipient_account_id` INT UNSIGNED NULL , PRIMARY KEY (`id`) , FOREIGN KEY (`account_id`) REFERENCES `%accountsTable%`(`id`) ON DELETE CASCADE )";
    private static final String SELECT_TRANSACTION = "SELECT * FROM `%transactionsTable%` WHERE `id` = ?";
    private static final String SELECT_TRANSACTIONS_BY_ACCOUNT = "SELECT * FROM `%transactionsTable%` WHERE `account_id` = ?";
    private static final String INSERT_TRANSACTION = "INSERT INTO `%transactionsTable%` (`account_id`, `amount`, `type`, `date`, `interest_rate`, `sender_account_id`, `recipient_account_id`) VALUES (?,?,?,?,?,?,?)";
    private static final String DELETE_TRANSACTION = "DELETE FROM `%transactionsTable%` WHERE `id` = ?";

    private static final String BANKS_TABLE_FIELD = "%banksTable%";
    private static final String ACCOUNTS_TABLE_FIELD = "%accountsTable%";
    private static final String TRANSACTIONS_TABLE_FIELD = "%transactionsTable%";

    private final String banksTable;
    private final String accountsTable;
    private final String transactionsTable;

    public SQLQueries(String banksTable, String accountsTable, String transactionsTable) {
        this.banksTable = banksTable;
        this.accountsTable = accountsTable;
        this.transactionsTable = transactionsTable;
    }

    public String getCreateBanksTable() {
        return CREATE_BANKS_TABLE.replace(BANKS_TABLE_FIELD, banksTable);
    }

    public String getInsertBank() {
        return INSERT_BANK.replace(BANKS_TABLE_FIELD, banksTable);
    }

    public String getSelectBank() {
        return SELECT_BANK.replace(BANKS_TABLE_FIELD, banksTable).replaceAll(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getUpdateBank() {
        return UPDATE_BANK.replace(BANKS_TABLE_FIELD, banksTable);
    }

    public String getDeleteBank() {
        return DELETE_BANK.replace(BANKS_TABLE_FIELD, banksTable);
    }

    public String getCreateAccountsTable() {
        return CREATE_ACCOUNTS_TABLE.replace(ACCOUNTS_TABLE_FIELD, accountsTable).replace(BANKS_TABLE_FIELD, banksTable);
    }

    public String getInsertAccount() {
        return INSERT_ACCOUNT.replace(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getSelectAccount() {
        return SELECT_ACCOUNT.replace(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getSelectAccountsByBank() {
        return SELECT_ACCOUNTS_BY_BANK.replace(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getUpdateAccount() {
        return UPDATE_ACCOUNT.replace(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getDeleteAccount() {
        return DELETE_ACCOUNT.replace(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getCreateTransactionsTable() {
        return CREATE_TRANSACTIONS_TABLE.replace(TRANSACTIONS_TABLE_FIELD, transactionsTable).replace(ACCOUNTS_TABLE_FIELD, accountsTable);
    }

    public String getSelectTransaction() {
        return SELECT_TRANSACTION.replace(TRANSACTIONS_TABLE_FIELD, transactionsTable);
    }

    public String getSelectTransactionsByAccount() {
        return SELECT_TRANSACTIONS_BY_ACCOUNT.replace(TRANSACTIONS_TABLE_FIELD, transactionsTable);
    }

    public String getInsertTransaction() {
        return INSERT_TRANSACTION.replace(TRANSACTIONS_TABLE_FIELD, transactionsTable);
    }

    public String getDeleteTransaction() {
        return DELETE_TRANSACTION.replace(TRANSACTIONS_TABLE_FIELD, transactionsTable);
    }
}
