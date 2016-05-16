<?php

    // To use this you MUST do the following!!
    // - create a file called db.conf that includes the following in a php block:
    // 
    // $dbhost = 'localhost';
    // $dbuser = 'your MySQL user id';
    // $dbpass = 'your MySQL password';
    // $dbname = 'your MySQL database name';
    // 

	// The db.conf file must be in the php include path OR you must explicitly define
	// the path in the include in the constructor below.

	// On babbage, your mysql information is contained in the mysql-details.txt file
	// located in your home directory.  Note that (on babbage) the name of your
	// MySQL database is the same as your MySQL user id.  That was done for the sake
	// of convenience NOT because it has to be that way.

	// On babbage, the database host (ip address) is localhost.  localhost is a convenient
	// way in the *nix operating systems to refer to the local machine.  This database
	// class will work fine with remotely hosted database systems if you supply the 
	// ip address and the server is set up to allow access from outside of the server.
	// Servers often restrict access to local programs for security reasons.  Babbage
	// restricts access to localhost only.

	// NOTES on using this class:

	//   This class is built on the mysqli object in php.  See:
	//   http://us3.php.net/manual/en/book.mysqli.php

	//   The saveRecord, saveNewRecord, and updateRecord methods assume that the table being
	//   used has two datetime columns called addDate and changeDate.  If you attempt to 
	//   use these methods on a table without those columns you will get an error.

	//   Creating a new Database object will automatically make a connection to the database
	//   in the constructor using the credentials you provide in db.conf
	//      $db = new Database();

	//   $mysqli = $db->getMysqli(); will get the underlying mysqli object that is provided by php.
	//   You do not have to ever use the mysqli object directly, but if you want to you can and you
	//   should refer to the php.net documentation for mysqli if you are interested.

	//   $escValue = $mysql->escapeString($value); will take a value and do the proper thing to
	//   return a string that has any characters escaped that could mess up an sql statement.
	//   If you are using one of the methods that accepts values (not constructing an sql statement
	//   directly), you should NOT escape the values.  That will be done automatically.  You should
	//   call this method on values that are going to be placed in an sql query string.

	//   $result = $db->runQuery($sql); will execute the sql in $sql and return the result object.
	//   Make sure $result has a value before using it!  if (!$result) {} will indicate an error.
	//   Here is documentation on all the things you can do with a result object:
	//   http://us3.php.net/manual/en/class.mysqli-result.php
	//   The most common thing to do is to fetch a row after doing a select:
	//   $row = $result->fetch_row();
	//   When you are done with a result you should close it:
	//   $result->close();
	//   Another common thing to do is get a series of rows resulting from a select.  And instead
	//   of just getting the values you can get the column names and associated values in
	//   an associative array:
	//   	$items = array();
	//		while ($row = $result->fetch_assoc()) {
	//			array_push($items, $row);
	//		}

	//   $db->commit();  commits inserts or updates of data in the database.
	//   $db->rollback(); rolls back the inserts or updates of data in the database.
	//   rollback is the opposite of commit

	//   NOTES on $condx (conditions) provided to a method:
	//    Some of the methods expect to receive one more conditions for identifying rows 
	//   in a table.  The $condx variable is an associative array that contains a list
	//   of column names and the values they are to be equal to.  All of the conditions
	//   provided are ANDed together.
	//   $condx = array('fname' => 'Dale', 'age' => 44); would indicate that a match should
	//   be made to rows that have the fname column equal to Dale AND the age column equal to 44.

	//   NOTES on $order (order) provided to a method:
	//   Some of the methods can be called (optionally) with an order specified. 
	//   An order determines the order in which rows are returned from a query.
	//   $order is an associative array where the keys are the names of columns in a table
	//   and the values are 'asc' for ascending and 'desc' for descending.
	//   $order = array('lname' => 'asc', 'fname' => 'asc'); will order the results
	//   where the lname and fname column values are both ascending.  This will give you
	//   a list of names in alphabetical order.

	//   $count = $db->getCount($table, $condx); counts the number of rows in a table
	//   that meet the condition provided.
	//   Example:  $count = $db->getCount('users', array('lastName' => 'Musser'));
	//   or
	//   $condx = array('lastName' => 'Musser');
	//   $count = $db->getCount('users', $condx);

	//   $maxVal = $db->getMax($table, $columnName, $condx);  gets the maximum value
	//   contained in a column in a table using the conditions specified.

	//   $records = $db->getRecords($table, $condx, $order); retrieves an associative
	//   array of rows that are in the table that meet the conditions in the order
	//   specified.
	//   Example:
	//   $records = $db->getRecords('users', array('gender' => 'f'), array('age' => 'asc'));
	//   This will return rows from the users table where the gender column value is f.  The 
	//   rows will be returned in an order based on the numerical age going from lowest to highest.

	//   $records = $db->getRecordsSubset($table, $condx, $order, $startPosition, $maxNum);
	//   This method will do the same thing as getRecords except it can be used to get a subset
	//   of the results.  $startPosition indicates which row number in the result to use as
	//   the start and $maxNum is the maximum number of rows to return.  Of course, less than
	//   $maxNum rows may be returned because there aren't enough rows to return $maxNum of them.

	//   $record = $db->getRecord($table, $condx); returns at most one record from the table
	//   specified using the conditions set by $condx.  If there is more than one row in a result
	//   set for a query ONLY the first one will be retrieved using getRecord.  getRecord is
	//   to be used where you are only expecting at most one result.  For example, if you want to 
	//   retrieve the user record for a user with a userId = 25, this would be a good choice 
	//   because only one user should have a userId of 25.  Of course, it is possible for no
	//   record to be returned if the conditions cannot be met.  For example, if there is no
	//   user with userId of 25.

	//   $db->saveRecord($table, $data, $condx); saves a record in a table using the data provided.
	//    $data is an associative array where the keys are the column names and the values are the
	//   data to be set in the associated columns.  DO NOT escape the values provided in $data.
	//   If a condition is specified, records that match the condition are updated if they exist.
	//   If no condition is specified or no record exists that matches the condition specified, then
	//   a new record (row) is created.  saveRecord, therefore, can update existing rows or create
	//   a new row.
	//   Example:
	//   $db->saveRecord('users', array('lname' => 'Smith'), array('userId' => 25));
	//   The example will update the lname column with the name Smith in the users
	//   table where the userId column has a value of 25.  Or, rather, the user
	//   who has an id of 25 will have their last name updated to Smith.

	//   $db->updateRecords($table, $data, $condx); updates records in a table using the data in $data
	//   and where the condition provided identifies the rows that are to be updated.
	//   $data is an associative array where the keys are the columns in the table and the values are
	//   the data to be placed in the associated columns.

	//   $db->saveNewRecord($table, $data); saves a new row in a table using the data provided in $data.
	//   $data is an associative array where the keys are the columns in the table and the values are
	//   the data to be placed in the associated columns.  saveNewRecord will fail if you are asking
	//   the database management system to create a row that may not be created. The reasons that
	//   may happen is because you are providing duplicate values for a column that must contain
	//   unique values or if you specify a column name for a column that doesn't exist.

	//   $id = $db->getInsertId(); returns the value of an AUTO_INCREMENT column when a new 
	//   row is created.  For example, if there is an id column in a users table that
	//   is auto incremented when a new user is added, getInsertId will return the id
	//   value for that user.

	//   $db->deleteRecords($table, $condx);  deletes rows in the specified table that match the
	//   conditions specified.

	//   Before you can work with tables, they must exist.  You could create a table using this
	//   class by executing an sql statement.  But, typically, you will create a table outside of
	//   your web application.  One way to do this is using the mysql program at the command line.
	//   To access mysql at the command line use the following:
	//   mysql -u userId -p databaseName
	//   and then enter your password when prompted.
	//   At the mysql prompt you can execute mysql statements.  Here is documentation on the
	//   create table syntax:
	//   http://dev.mysql.com/doc/refman/5.0/en/create-table.html
	//   Here is an example:
	//   create table users (
	//     id BIGINT(20)UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	//     userName VARCHAR(20)NOT NULL UNIQUE,
	//     lastName VARCHAR(20),
	//     firstName VARCHAR(20),
	//     email VARCHAR(60),
	//     addDate DATETIME NOT NULL,
	//     changeDate DATETIME NOT NULL
	//   );
	//   The semicolon at the end of the sql statement is ONLY required when you enter the sql
	//   at the command prompt in mysql.  You DO NOT put a semicolon at the end of the statement
	//   if you are supplying the statement in a php script.

	//   You can remove a table by executing a drop table statement.
	//   drop table users;
	//   will delete a table named users from the current database.

	//   To list all the tables in the current database use:
	//   show tables;

	//   To get a description of a table in the current database use:
	//   describe tableName;
	//   where tableName is the name of a table that exists.


	// list of DatabaseException codes/messages
	// mysqli_connect_errno() - database connect error (construct)
	// mysqli_error() - message is what happened including SQL if applies (elsewhere)

	class DatabaseException extends Exception {
		protected $sql;
		
		public function __construct($message = null, $code = 0, $sql = null) {
			
			if ($sql) {
				$this->sql = $sql;
				$message .= ": SQL='{$sql}'";
			}
		
			parent::__construct($message, $code);
		}
		
		public function getSql() {
			return $this->sql;
		}
	}

	class Database {
		protected $env;
		protected $mysqli;
		protected $insertId;
		
		
		public function __construct() {
			include ('db.conf');
			$this->mysqli = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
			if (mysqli_connect_errno()) {
				throw new DatabaseException('database connect error', mysqli_connect_errno(), '');
			}		
		
			$this->mysqli->autocommit(false);
		}
		
		public function getMysqli() {
			return $this->mysqli;
		}
		
		public function escapeString($value) {
			$mysqli = $this->mysqli;
			return $mysqli->escape_string($value);
		}
		
		public function runQuery($sql) {
			$mysqli = $this->mysqli;
			$result = $mysqli->query($sql);
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			return $result;
		}
		
		public function commit() {
			$mysqli = $this->mysqli;
			$mysqli->commit();
		}

		public function rollback() {
			$mysqli = $this->mysqli;
			$mysqli->rollback();
		}
		
		public function getCount($table, $condx) {
			$mysqli = $this->mysqli;
			
			$sql = "select count(*) from $table";
			
			if ($condx) {
				$sql .= " where";
				$first = TRUE;
				foreach ($condx as $key => $value) {
					if (!$first) {
						$sql .= " AND";
					} else {
						$first = FALSE;
					}
					$value = $mysqli->escape_string($value);
					$sql .= " $key = '$value'";
				}
			}
			
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			$row = $result->fetch_row();
			$result->close();

			return $row[0];  // the count
		}
		
		public function getMax($table, $columnName, $condx) {
			$mysqli = $this->mysqli;
			
			$sql = "select max($columnName) from $table";

			if ($condx) {
				$sql .= " where";
				$first = TRUE;
				foreach ($condx as $key => $value) {
					if (!$first) {
						$sql .= " AND";
					} else {
						$first = FALSE;
					}
					$value = $mysqli->escape_string($value);
					$sql .= " $key = '$value'";
				}
			}
			
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			$row = $result->fetch_row();
			$result->close();

			return $row[0];  // the max value			
		}
		
		public function getRecords($table, $condx, $order) {
			$mysqli = $this->mysqli;
			
			$sql = "select * from $table";
			
			if ($condx) {
				$sql .= " where";
				$first = TRUE;
				foreach ($condx as $key => $value) {
					if (!$first) {
						$sql .= " AND";
					} else {
						$first = FALSE;
					}
					$value = $mysqli->escape_string($value);
					$sql .= " $key = '$value'";
				}
			}
			
			if ($order) {
				$sql .= " order by";
				foreach ($order as $key => $value) {
					$sql .= " $key $value,";
				}
				$sql = rtrim($sql, ",");
			}
	
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			$items = array();
			while ($row = $result->fetch_assoc()) {
				array_push($items, $row);
			}
			
			$result->close();			
			
			return $items;
		}
		
		public function getMultiTableRecords($tables, $condx, $condmt, $order) { // added by curtis
			$mysqli = $this->mysqli;
			
			$sql = 'select * from ';
			$first = true;
			foreach($tables as $value){
				if (!$first) {
					$sql .= ", ";
				} else {
					$first = FALSE;
				}
				$sql .= $value;
			}
			
			if ($condx) {
				$sql .= ' where';
				$first = TRUE;
				foreach ($condx as $key => $value) {
					if (!$first) {
						$sql .= ' AND';
					} else {
						$first = FALSE;
					}
					$value = $mysqli->escape_string($value);
					$sql .= " $key = '$value'";
				}
			}
			
			if ($condmt) {
				foreach ($condmt as $key => $value) {
					$sql .= ' AND';
					$value = $mysqli->escape_string($value);
					$sql .= " $key = $value";
				}
			}
			
			if ($order) {
				$sql .= " order by";
				foreach ($order as $key => $value) {
					$sql .= " $key $value,";
				}
				$sql = rtrim($sql, ",");
			}
	
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			$items = array();
			while ($row = $result->fetch_assoc()) {
				array_push($items, $row);
			}
			
			$result->close();			
			
			return $items;
		}
		
		public function getColumn($col, $table, $condx, $order) { // added by curtis
			$mysqli = $this->mysqli;
			
			$sql = "select $col from $table";
			
			if ($condx) {
				$sql .= " where";
				$first = TRUE;
				foreach ($condx as $key => $value) {
					if (!$first) {
						$sql .= " AND";
					} else {
						$first = FALSE;
					}
					$value = $mysqli->escape_string($value);
					$sql .= " $key = '$value'";
				}
			}
			
			if ($order) {
				$sql .= " order by";
				foreach ($order as $key => $value) {
					$sql .= " $key $value,";
				}
				$sql = rtrim($sql, ",");
			}
	
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			$items = array();
			while ($row = $result->fetch_assoc()) {
				array_push($items, $row[$col]);
			}
			
			$result->close();			
			
			return $items;
		}
		
		// startPosition starts at 1 for first record
		public function getRecordsSubset($table, $condx, $order, $startPosition, $maxNum) {
			$mysqli = $this->mysqli;
			
			$sql = "select * from $table";
			
			if ($condx) {
				$sql .= " where";
				$first = TRUE;
				foreach ($condx as $key => $value) {
					if (!$first) {
						$sql .= " AND";
					} else {
						$first = FALSE;
					}
					$value = $mysqli->escape_string($value);
					$sql .= " $key = '$value'";
				}
			}
			
			if ($order) {
				$sql .= " order by";
				foreach ($order as $key => $value) {
					$sql .= " $key $value,";
				}
				$sql = rtrim($sql, ",");
			}
	
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			$currentPosition = 0;
			$currentCount = 0;
			
			$items = array();
			while ( ($row = $result->fetch_assoc()) && ($currentCount < $maxNum)) {
				$currentPosition++;
				if ($currentPosition >= $startPosition) {
					array_push($items, $row);
					$currentCount++;
				}
			}
			
			$result->close();			
			
			return $items;
		}
		
		public function getRecord($table, $condx) {
			$mysqli = $this->mysqli;
			
			$sql = "select * from $table where";
			$first = TRUE;
			foreach ($condx as $key => $value) {
				if (!$first) {
					$sql .= " AND";
				} else {
					$first = FALSE;
				}
				$value = $mysqli->escape_string($value);
				$sql .= " $key = '$value'";
			}
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			$record = $result->fetch_assoc();
			$result->close();
			return $record;
		}
		
		public function saveRecord($table, $data, $condx) {
			// if condx set, check if record exists
			if ($condx) {
				try {
					$record = $this->getRecord($table, $condx); 
				} catch (Exception $e) {
					throw $e;
				}
				if ($record) {
					try {
						$this->updateRecords($table, $data, $condx);
					} catch(Exception $e) {
						throw $e;
					}
					// commit!
					$mysqli = $this->mysqli;
					$mysqli->commit();
					return;
				}
			}
			
			// no condx or record doesn't exist, save new record
			try {
				$this->saveNewRecord($table, $data);
			} catch(Exception $e) {
				throw $e;
			}
		}
		
		public function updateRecords($table, $data, $condx) {
			
			$mysqli = $this->mysqli;
		
			$sql = "update $table set";
			$first = TRUE;
			foreach ($data as $key => $value) {
				if (!$first) {
					$sql .= ',';
				} else {
					$first = FALSE;
				}
				$value = $mysqli->escape_string($value);
				$sql .= " $key = '$value'";
			}
			$sql .= ', changeDate=NOW() where';
			
			$first = TRUE;
			foreach ($condx as $key => $value) {
				if (!$first) {
					$sql .= " AND";
				} else {
					$first = FALSE;
				}
				$value = $mysqli->escape_string($value);
				$sql .= " $key = '$value'";
			}
			$result = $mysqli->query($sql);
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			// commit!
			$mysqli->commit();
		}
		
		public function saveNewRecord($table, $data) {
		
			$mysqli = $this->mysqli;

			$first = TRUE;
			$namesStr = '(';
			$valuesStr = '(';
			foreach ($data as $key => $value) {
				if (!$first) {
					$namesStr .= ',';
					$valuesStr .= ',';
				} else {
					$first = FALSE;
				}
				$namesStr .= " $key";
				$value = $mysqli->escape_string($value);
				$valuesStr .= " '$value'";
			}
			$namesStr .= ', addDate, changeDate)';
			$valuesStr .= ', NOW(), NOW())';
			$sql = "insert into $table $namesStr values $valuesStr";
			$result = $mysqli->query($sql);
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			// get insert id
			$this->insertId = $mysqli->insert_id;
			
			// commit!
			$mysqli->commit();
		}
		
		public function getInsertId() {

			return $this->insertId;
		}
		
		public function deleteRecords($table, $condx) {
			$mysqli = $this->mysqli;
			
			$sql = "delete from $table where";
			$first = TRUE;
			foreach ($condx as $key => $value) {
				if (!$first) {
					$sql .= " AND";
				} else {
					$first = FALSE;
				}
				$value = $mysqli->escape_string($value);
				$sql .= " $key = '$value'";
			}
			$result = $mysqli->query($sql);
			
			if (!$result) {
				throw new DatabaseException(null, null, $sql);
			}
			
			// commit!
			$mysqli->commit();
		}
	

	}
?>