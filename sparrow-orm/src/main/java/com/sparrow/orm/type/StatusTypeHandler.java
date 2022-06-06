package com.sparrow.orm.type;

import com.sparrow.protocol.enums.STATUS_RECORD;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author by harry
 */
public class StatusTypeHandler implements TypeHandler<STATUS_RECORD> {
    @Override public void setParameter(PreparedStatement ps, int i, STATUS_RECORD parameter) throws SQLException {
        ps.setInt(i, parameter.ordinal());
    }

    @Override public STATUS_RECORD getResult(ResultSet rs, String columnName) throws SQLException {
        return STATUS_RECORD.values()[rs.getInt(columnName)];
    }

    @Override public STATUS_RECORD getResult(ResultSet rs, int columnIndex) throws SQLException {
        return STATUS_RECORD.values()[rs.getInt(columnIndex)];
    }

    @Override public STATUS_RECORD getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return STATUS_RECORD.values()[cs.getInt(columnIndex)];
    }
}
