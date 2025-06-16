package com.harusari.chainware.vendor.config;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class VendorTypeEnumTypeHandler extends BaseTypeHandler<VendorType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, VendorType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public VendorType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : VendorType.valueOf(value);
    }

    @Override
    public VendorType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : VendorType.valueOf(value);
    }

    @Override
    public VendorType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : VendorType.valueOf(value);
    }
}
