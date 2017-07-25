package morcheca.dao.util;

import org.unitils.core.UnitilsException;
import org.unitils.core.dbsupport.HsqldbDbSupport;
import org.unitils.thirdparty.org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;


public class CustomHsqldbDbSupport extends HsqldbDbSupport {

	@Override
	public Set<String> getTriggerNames() {
		return this.getSQLHandler().getItemsAsStringSet("select TRIGGER_NAME from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = \'" + this.getSchemaName() + "\'");
	}

	@Override
	public void disableReferentialConstraints() {
		Connection connection = null;
		Statement queryStatement = null;
		Statement alterStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.getSQLHandler().getDataSource().getConnection();
			queryStatement = connection.createStatement();
			alterStatement = connection.createStatement();
			resultSet = queryStatement.executeQuery("select TABLE_NAME, CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE = \'FOREIGN KEY\' AND CONSTRAINT_SCHEMA = \'" + this.getSchemaName() + "\'");

			while(resultSet.next()) {
				String e = resultSet.getString("TABLE_NAME");
				String constraintName = resultSet.getString("CONSTRAINT_NAME");
				alterStatement.executeUpdate("alter table " + this.qualified(e) + " drop constraint " + this.quoted(constraintName));
			}
		} catch (Exception var10) {
			throw new UnitilsException("Error while disabling not referential constraints on schema " + this.getSchemaName(), var10);
		} finally {
			DbUtils.closeQuietly(queryStatement);
			DbUtils.closeQuietly(connection, alterStatement, resultSet);
		}

	}

	@Override
	protected void disableCheckAndUniqueConstraints() {
		Connection connection = null;
		Statement queryStatement = null;
		Statement alterStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.getSQLHandler().getDataSource().getConnection();
			queryStatement = connection.createStatement();
			alterStatement = connection.createStatement();
			resultSet = queryStatement.executeQuery("select TABLE_NAME, CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE IN (\'CHECK\', \'UNIQUE\') AND CONSTRAINT_SCHEMA = \'" + this.getSchemaName() + "\'");

			while(resultSet.next()) {
				String e = resultSet.getString("TABLE_NAME");
				String constraintName = resultSet.getString("CONSTRAINT_NAME");
				alterStatement.executeUpdate("alter table " + this.qualified(e) + " drop constraint " + this.quoted(constraintName));
			}
		} catch (Exception var10) {
			throw new UnitilsException("Error while disabling check and unique constraints on schema " + this.getSchemaName(), var10);
		} finally {
			DbUtils.closeQuietly(queryStatement);
			DbUtils.closeQuietly(connection, alterStatement, resultSet);
		}

	}

}
