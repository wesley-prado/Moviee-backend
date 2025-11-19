package com.codemages.Moviee.auth.security.config;

import org.springframework.core.log.LogMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

/**
 * Custom PersistentTokenRepository implementation that uses the "auth" schema
 * for the persistent_logins table.
 */
public class AuthSchemaTokenRepository extends JdbcTokenRepositoryImpl {
  private static final String INSERT_TOKEN_SQL =
    "insert into auth.persistent_logins (username, series, token, last_used) values(?,?,?,?)";
  private static final String UPDATE_TOKEN_SQL =
    "update auth.persistent_logins set token = ?, last_used = ? where series = ?";
  private static final String SELECT_TOKEN_SQL =
    "select username, series, token, last_used from auth.persistent_logins where series = ?";
  private static final String REMOVE_USER_TOKENS_SQL =
    "delete from auth.persistent_logins where username = ?";

  @Override
  public void createNewToken(PersistentRememberMeToken token) {
    getJdbcTemplate().update(
      INSERT_TOKEN_SQL,
      token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate()
    );
  }

  @Override
  public void updateToken(String series, String tokenValue, Date lastUsed) {
    getJdbcTemplate().update( UPDATE_TOKEN_SQL, tokenValue, lastUsed, series );
  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    try {
      return getJdbcTemplate().queryForObject(
        SELECT_TOKEN_SQL,
        (rs, rowNum) -> new PersistentRememberMeToken(
          rs.getString( "username" ),
          rs.getString( "series" ),
          rs.getString( "token" ),
          rs.getTimestamp( "last_used" )
        )
        , seriesId
      );
    } catch (EmptyResultDataAccessException ex) {
      this.logger.debug(
        LogMessage.format(
          "Querying token for series '%s' returned no results.",
          seriesId
        ), ex
      );
    } catch (IncorrectResultSizeDataAccessException ex) {
      this.logger.error( LogMessage.format(
        "Querying token for series '%s' returned more than one value. Series" + " should be unique",
        seriesId
      ) );
    } catch (DataAccessException ex) {
      this.logger.error( "Failed to load token for series " + seriesId, ex );
    }
    return null;
  }

  @Override
  public void removeUserTokens(String username) {
    getJdbcTemplate().update( REMOVE_USER_TOKENS_SQL, username );
  }
}
