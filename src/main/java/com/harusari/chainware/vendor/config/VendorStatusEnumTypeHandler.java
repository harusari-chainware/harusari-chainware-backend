package com.harusari.chainware.vendor.config;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class VendorStatusEnumTypeHandler extends BaseTypeHandler<VendorStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, VendorStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public VendorStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : VendorStatus.valueOf(value);
    }

    @Override
    public VendorStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : VendorStatus.valueOf(value);
    }

    @Override
    public VendorStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : VendorStatus.valueOf(value);
    }
}
