package org.derewah.derecounter.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.derewah.derecounter.objects.CompanyBook;
import org.derewah.derecounter.objects.RegistryAction;

import java.sql.SQLException;

public class Database {

	private final Dao<CompanyBook, String> companyBooksDao;

	private final Dao<RegistryAction, Integer> registryActionsDao;

	public Database(String path) throws SQLException {
		ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:"+path);
		TableUtils.createTableIfNotExists(connectionSource, CompanyBook.class);
		TableUtils.createTableIfNotExists(connectionSource, RegistryAction.class);
		companyBooksDao = DaoManager.createDao(connectionSource, CompanyBook.class);
		registryActionsDao = DaoManager.createDao(connectionSource, RegistryAction.class);
	}

	public CompanyBook addCompanyBook(String companyName) throws SQLException {
		CompanyBook companyBook = new CompanyBook();
		companyBook.setName(companyName);
		companyBooksDao.create(companyBook);
		return companyBook;
	}

	public CompanyBook getCompanyBook(String companyName) throws SQLException {
		return companyBooksDao.queryForId(companyName);
	}

	public void deleteCompanyBook(String companyName) throws SQLException {
		companyBooksDao.deleteById(companyName);
	}

	public void updateCompanyBook(CompanyBook companyBook) throws SQLException {
		companyBooksDao.update(companyBook);
	}
}
