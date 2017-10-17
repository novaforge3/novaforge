package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec;

/**
 * Result of a command execution.
 * 
 * @author B-Martinelli
 */
public class ExecResult
{
  /** Exit code of the command. */
  private int    exitCode  = -1;

  /** Textual result of the command. */
  private String cmdResult = "";

  /**
   * @param exitCode
   * @param cmdResult
   */
  public ExecResult()
  {
    super();
  }

  /**
   * @param exitCode
   * @param cmdResult
   */
  public ExecResult(int exitCode, String cmdResult)
  {
    super();
    this.exitCode = exitCode;
    this.cmdResult = cmdResult;
  }

  /**
   * @return the exitCode
   */
  public int getExitCode()
  {
    return exitCode;
  }

  /**
   * @param exitCode
   *          the exitCode to set
   */
  public void setExitCode(int exitCode)
  {
    this.exitCode = exitCode;
  }

  /**
   * @return the cmdResult
   */
  public String getCmdResult()
  {
    return cmdResult;
  }

  /**
   * @param cmdResult
   *          the cmdResult to set
   */
  public void setCmdResult(String cmdResult)
  {
    this.cmdResult = cmdResult;
  }

}
