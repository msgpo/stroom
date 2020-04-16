/*
 * This file is generated by jOOQ.
 */
package stroom.authentication.impl.db.jooq;


import javax.annotation.processing.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;

import stroom.authentication.impl.db.jooq.tables.Account;
import stroom.authentication.impl.db.jooq.tables.JsonWebKey;
import stroom.authentication.impl.db.jooq.tables.OauthClient;
import stroom.authentication.impl.db.jooq.tables.Token;
import stroom.authentication.impl.db.jooq.tables.TokenType;


/**
 * A class modelling indexes of tables of the <code>stroom</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index ACCOUNT_EMAIL = Indexes0.ACCOUNT_EMAIL;
    public static final Index ACCOUNT_PRIMARY = Indexes0.ACCOUNT_PRIMARY;
    public static final Index JSON_WEB_KEY_JSON_WEB_KEY_FK_TOKEN_TYPE_ID = Indexes0.JSON_WEB_KEY_JSON_WEB_KEY_FK_TOKEN_TYPE_ID;
    public static final Index JSON_WEB_KEY_PRIMARY = Indexes0.JSON_WEB_KEY_PRIMARY;
    public static final Index OAUTH_CLIENT_CLIENT_ID = Indexes0.OAUTH_CLIENT_CLIENT_ID;
    public static final Index OAUTH_CLIENT_NAME = Indexes0.OAUTH_CLIENT_NAME;
    public static final Index OAUTH_CLIENT_PRIMARY = Indexes0.OAUTH_CLIENT_PRIMARY;
    public static final Index TOKEN_PRIMARY = Indexes0.TOKEN_PRIMARY;
    public static final Index TOKEN_TOKEN_FK_ACCOUNT_ID = Indexes0.TOKEN_TOKEN_FK_ACCOUNT_ID;
    public static final Index TOKEN_TOKEN_FK_TOKEN_TYPE_ID = Indexes0.TOKEN_TOKEN_FK_TOKEN_TYPE_ID;
    public static final Index TOKEN_TYPE_PRIMARY = Indexes0.TOKEN_TYPE_PRIMARY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index ACCOUNT_EMAIL = Internal.createIndex("email", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.EMAIL }, true);
        public static Index ACCOUNT_PRIMARY = Internal.createIndex("PRIMARY", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.ID }, true);
        public static Index JSON_WEB_KEY_JSON_WEB_KEY_FK_TOKEN_TYPE_ID = Internal.createIndex("json_web_key_fk_token_type_id", JsonWebKey.JSON_WEB_KEY, new OrderField[] { JsonWebKey.JSON_WEB_KEY.FK_TOKEN_TYPE_ID }, false);
        public static Index JSON_WEB_KEY_PRIMARY = Internal.createIndex("PRIMARY", JsonWebKey.JSON_WEB_KEY, new OrderField[] { JsonWebKey.JSON_WEB_KEY.ID }, true);
        public static Index OAUTH_CLIENT_CLIENT_ID = Internal.createIndex("client_id", OauthClient.OAUTH_CLIENT, new OrderField[] { OauthClient.OAUTH_CLIENT.CLIENT_ID }, true);
        public static Index OAUTH_CLIENT_NAME = Internal.createIndex("name", OauthClient.OAUTH_CLIENT, new OrderField[] { OauthClient.OAUTH_CLIENT.NAME }, true);
        public static Index OAUTH_CLIENT_PRIMARY = Internal.createIndex("PRIMARY", OauthClient.OAUTH_CLIENT, new OrderField[] { OauthClient.OAUTH_CLIENT.ID }, true);
        public static Index TOKEN_PRIMARY = Internal.createIndex("PRIMARY", Token.TOKEN, new OrderField[] { Token.TOKEN.ID }, true);
        public static Index TOKEN_TOKEN_FK_ACCOUNT_ID = Internal.createIndex("token_fk_account_id", Token.TOKEN, new OrderField[] { Token.TOKEN.FK_ACCOUNT_ID }, false);
        public static Index TOKEN_TOKEN_FK_TOKEN_TYPE_ID = Internal.createIndex("token_fk_token_type_id", Token.TOKEN, new OrderField[] { Token.TOKEN.FK_TOKEN_TYPE_ID }, false);
        public static Index TOKEN_TYPE_PRIMARY = Internal.createIndex("PRIMARY", TokenType.TOKEN_TYPE, new OrderField[] { TokenType.TOKEN_TYPE.ID }, true);
    }
}
