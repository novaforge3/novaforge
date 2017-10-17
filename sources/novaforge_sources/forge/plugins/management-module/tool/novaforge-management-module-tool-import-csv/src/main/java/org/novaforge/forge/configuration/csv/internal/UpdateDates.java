/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.configuration.csv.internal;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateDates {

   private static final String JDBC_MYSQL = "jdbc:mysql://";

   private static final Logger LOG = Logger.getLogger("UpdateDates");
	private static final String TEST_CONNECTION = "-testConnection";
//   private String host = "localhost";
	private              String host            = "safranInteg";
	private              String databaseName    = "plugin_management_module";
	//   private String login = "root";
	private              String login           = "novaforge";
	//   private String pass = "root";
	private              String pass            = "novaforge";

	public UpdateDates()
	{

		LOG.log(Level.INFO,
						"Defaults Params : Host = " + host + ", login = " + login + ", pass = " + pass + ", databaseName = "
								+ databaseName);

		readConfigFile();
	}

	private void readConfigFile()
	{
		File f = new File("updateDates.cfg");
		if (f.exists())
		{
		  DataInputStream in = null;
		  BufferedReader br = null;
			try
			{

				FileInputStream fstream;
				fstream = new FileInputStream(f);
				// Get the object of DataInputStream
				in = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				//Read File Line By Line
				while ((strLine = br.readLine()) != null)
				{

					strLine = strLine.trim();

					//Comment's Lines
					if (strLine.length() == 0 || strLine.equalsIgnoreCase("") || strLine.startsWith("//") || strLine
																																																			 .startsWith("#"))
					{
						continue;
					}

					String[] keyVal = strLine.split("=");

					if (keyVal[0].equalsIgnoreCase(Params.host.name()))
					{
						host = keyVal[1];
					}
					else if (keyVal[0].equalsIgnoreCase(Params.login.name()))
					{
						login = keyVal[1];
					}
					else if (keyVal[0].equalsIgnoreCase(Params.pass.name()))
					{
						pass = keyVal[1];
					}
					else if (keyVal[0].equalsIgnoreCase(Params.databaseName.name()))
					{
						databaseName = keyVal[1];
					}
				}
			}
			catch (FileNotFoundException e)
			{
				LOG.log(Level.INFO, "No config File, use Default",e);
			}
			catch (IOException e)
			{
				LOG.log(Level.SEVERE, e.getMessage(), e);
			}
			finally
			{
        if (br != null)
        {
          try
          {
            br.close();
          }
          catch (IOException e)
          {
            LOG.log(Level.SEVERE, e.getMessage(), e);
          }
        }
        if ( in != null )
        {
         try
          {
            in.close();
          }
          catch (IOException e)
          {
            LOG.log(Level.SEVERE, e.getMessage(), e);
          }
        }
			}
		}
		else
		{
			LOG.log(Level.INFO, "No config File, use Default");
		}

		LOG.log(Level.INFO,
						"Params : Host = " + host + ", login = " + login + ", pass = " + pass + ", databaseName = " + databaseName);
	}

	public static void main(String[] args)
	{
		UpdateDates majDate = new UpdateDates();

		if (args != null && args.length > 0 && args[0] != null && args[0].equalsIgnoreCase(TEST_CONNECTION))
		{
			majDate.testConnection();
		}
		else
		{
			majDate.run();
		}
	}

	private void testConnection()
	{

		String pilote = "com.mysql.jdbc.Driver";

		Connection connexion   = null;
		Statement  instruction = null;

		try
		{
			Class.forName(pilote);

			connexion = DriverManager.getConnection(JDBC_MYSQL + host + "/" + databaseName, login, pass);

			instruction = connexion.createStatement();

			ResultSet resultat = instruction.executeQuery("SELECT * FROM project");

			while (resultat.next())
			{

				LOG.info("id = " + resultat.getString(1) + "projectName = " + resultat.getString(2));
			}

			LOG.log(Level.INFO, "Connection Successfull !");

		}
		catch (Exception e)
		{
			LOG.log(Level.SEVERE, "echec pilote : " + e.getMessage(), e);
		}
		finally
		{
			try
			{
			  if (instruction != null) 
			  {
			    instruction.close();
			  }
			  if (connexion != null)
	      {
	         connexion.close();
	      }
			}
			catch (SQLException e)
			{
				LOG.log(Level.SEVERE, "error when closing connexion : " + e.getMessage(), e);
			}
		}
	}

	public void run()
	{

		this.majIterations();
		this.majLots();
		this.majMarkers();

		LOG.log(Level.INFO, "UpdateDates Done.");

	}

	private void majIterations()
	{

		String pilote = "com.mysql.jdbc.Driver";

		Connection connexion = null;
		Statement instruction = null;
		try {
			Class.forName(pilote);

			connexion = DriverManager.getConnection(JDBC_MYSQL
					+ host + "/" + databaseName, login, pass);

			instruction = connexion.createStatement();

			ResultSet resultat = instruction.executeQuery("SELECT * FROM iteration");

			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			Map<Long, Date> startDates = new HashMap<Long, Date>();
			Map<Long, Date> endDates = new HashMap<Long, Date>();

			while (resultat.next()) {

				LOG.log(Level.INFO, "---------------------------");

				c1.setTime(resultat.getDate("start_date"));
				c2.setTime(resultat.getDate("end_date"));
				c1.add(Calendar.YEAR, -1000);
				c2.add(Calendar.YEAR, -1000);
				startDates.put(resultat.getLong("id"), c1.getTime());
				endDates.put(resultat.getLong("id"), c2.getTime());
			}

			for (Long id : startDates.keySet()) {
				instruction.executeUpdate("UPDATE iteration SET start_date='"
						+ format.format(startDates.get(id)) + "' , end_date='"
						+ format.format(endDates.get(id)) + "' WHERE id=" + id);

			}

			LOG.log(Level.INFO, "Iterations Updated");

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "echec pilote : " + e.getMessage(), e);
		}finally{
		  try
      {
        if (instruction != null)
        {
          instruction.close();
        }
        if (connexion != null)
        {
					connexion.close();
		    }
      } catch (SQLException e) {
         LOG.log(Level.SEVERE, "error when closing connexion : " + e.getMessage(), e);
      }
		}
	}

	private void majLots()
	{

		String pilote = "com.mysql.jdbc.Driver";

		Connection connexion = null;
		Statement instruction = null;
		try {
			Class.forName(pilote);

			connexion = DriverManager.getConnection(JDBC_MYSQL
					+ host + "/" + databaseName, login, pass);

			instruction = connexion.createStatement();

			ResultSet resultat = instruction.executeQuery("SELECT * FROM lot WHERE description <> 'Lot initial'");

			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			Map<Long, Date> startDates = new HashMap<Long, Date>();
			Map<Long, Date> endDates = new HashMap<Long, Date>();

			while (resultat.next()) {

				LOG.log(Level.INFO, "---------------------------");

				c1.setTime(resultat.getDate("start_date"));
				c2.setTime(resultat.getDate("end_date"));
				c1.add(Calendar.YEAR, -1000);
				c2.add(Calendar.YEAR, -1000);
				startDates.put(resultat.getLong("id"), c1.getTime());
				endDates.put(resultat.getLong("id"), c2.getTime());
			}

			for (Long id : startDates.keySet()) {
				instruction.executeUpdate("UPDATE lot SET start_date='"
						+ format.format(startDates.get(id)) + "' , end_date='"
						+ format.format(endDates.get(id)) + "' WHERE id=" + id);

			}

			LOG.log(Level.INFO, "lots Updated");

		}
		catch (Exception e)
		{
			LOG.log(Level.SEVERE, "echec pilote : " + e.getMessage(), e);
		}finally{
			try
			{ 
			  if (instruction != null)
        {
			    instruction.close();
        }
			  if (connexion != null)
			  {
			    connexion.close();
			  }
      } catch (SQLException e) {
         LOG.log(Level.SEVERE, "error when closing connexion : " + e.getMessage(), e);
      }
		}
	}

	private void majMarkers() {

		String pilote = "com.mysql.jdbc.Driver";

		Connection connexion = null;
		Statement instruction = null;
		try {
			Class.forName(pilote);

			connexion = DriverManager.getConnection(JDBC_MYSQL
					+ host + "/" + databaseName, login, pass);

			instruction = connexion.createStatement();

			ResultSet resultat = instruction
					.executeQuery("SELECT * FROM marker");

			Calendar c = Calendar.getInstance();

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			Map<Long, Date> dates = new HashMap<Long, Date>();

			while (resultat.next()) {

	         LOG.log(Level.INFO, "---------------------------");

				c.setTime(resultat.getDate("date"));
				c.add(Calendar.YEAR, -1000);
				dates.put(resultat.getLong("id"), c.getTime());
			}

			for (Long id : dates.keySet()) {
				instruction.executeUpdate("UPDATE marker SET date='"
						+ format.format(dates.get(id)) + "' WHERE id=" + id);

			}

			LOG.log(Level.INFO, "markers Updated");

		} catch (Exception e) {
         LOG.log(Level.SEVERE, "echec pilote : " + e.getMessage(), e);
		}finally{
      try {
        if (instruction != null){
          instruction.close();
        }
        if (connexion != null){
         connexion.close();
        }
      } catch (SQLException e) {
         LOG.log(Level.SEVERE, "error when closing connexion : " + e.getMessage(), e);
      }
		}
	}

	private enum Params
	{
		host,
		login,
		pass,
		databaseName;

		@Override
		public String toString()
		{
			return this.name();
		}
	}
}