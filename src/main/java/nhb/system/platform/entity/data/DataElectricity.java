/**
 * Project Name:nhb-platform
 * File Name:DataElectricity.java
 * Package Name:nhb.system.platform.entity.data
 * Date:2017年9月21日下午1:46:20
 * Copyright (c) 2017, lorisun@live.com All Rights Reserved.
 */

package nhb.system.platform.entity.data;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author xuyahui
 * @ClassName:DataElectricity
 * @Function: ADD FUNCTION.
 * @Reason: ADD REASON.
 * @Date: 2017年9月21日 下午1:46:20
 * @see
 * @since JDK 1.7
 */

@Document(collection = "data_electricity")
public class DataElectricity {
	@Id
	@Excel(name = "标识")
	private UUID id;

	@Indexed
	@Field("device_id")
	@Excel(name = "设备Id", orderNum = "0")
	private String deviceId;

	@Indexed
	@Field("read_time")
	@Excel(name = "时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
	private Date readTime;

	@Field("electricity_type")
	@Excel(name = "类型")
	private String electricityType;

	@Field("frequency")
	@Excel(name = "频率")
	private Double frequency;

	@Field("voltage")
	@Excel(name = "电压")
	private Double voltage;

	@Field("voltage_a")
	@Excel(name = "A相电压")
	private Double voltageA;

	@Field("voltage_b")
	@Excel(name = "B相电压")
	private Double voltageB;

	@Field("voltage_c")
	@Excel(name = "C相电压")
	private Double voltageC;

	@Field("voltage_a_b")
	@Excel(name = "AB相电压")
	private Double voltageAB;

	@Field("voltage_b_c")
	@Excel(name = "BC相电压")
	private Double voltageBC;

	@Field("voltage_c_a")
	@Excel(name = "CA相电压")
	private Double voltageCA;

	@Field("current")
	@Excel(name = "电流")
	private Double current;

	@Field("current_a")
	@Excel(name = "A相电流")
	private Double currentA;

	@Field("current_b")
	@Excel(name = "B相电流")
	private Double currentB;

	@Field("current_c")
	@Excel(name = "C相电流")
	private Double currentC;

	@Field("kw")
	@Excel(name = "总有功功率")
	private Double kw;

	@Field("kw_a")
	@Excel(name = "A相有功功率")
	private Double kwA;

	@Field("kw_b")
	@Excel(name = "B相有功功率")
	private Double kwB;

	@Field("kw_c")
	@Excel(name = "C相有功功率")
	private Double kwC;

	@Field("kvar")
	@Excel(name = "总无功功率")
	private Double kvar;

	@Field("kvar_a")
	@Excel(name = "A相无功功率")
	private Double kvarA;

	@Field("kvar_b")
	@Excel(name = "B相无功功率")
	private Double kvarB;

	@Field("kvar_c")
	@Excel(name = "C相无功功率")
	private Double kvarC;

	@Field("kva")
	@Excel(name = "总视在功率")
	private Double kva;

	@Field("kva_a")
	@Excel(name = "A相视在功率")
	private Double kvaA;

	@Field("kva_b")
	@Excel(name = "B相视在功率")
	private Double kvaB;

	@Field("kva_c")
	@Excel(name = "C相视在功率")
	private Double kvaC;

	@Field("kwh")
	@Excel(name = "有功总电能")
	private Double kwh;

	@Field("kwh_forward")
	@Excel(name = "正向有功总电能")
	private Double kwhForward;

	@Field("kwh_reverse")
	@Excel(name = "反向有功总电能")
	private Double kwhReverse;

	@Field("kvarh1")
	@Excel(name = "组合无功1总电能")
	private Double kvarh1;

	@Field("kvarh2")
	@Excel(name = "组合无功2总电能")
	private Double kvarh2;

	@Field("power_factor")
	@Excel(name = "总功率因数")
	private Double powerFactor;

	@Field("power_factor_a")
	@Excel(name = "A相功率因数")
	private Double powerFactorA;

	@Field("power_factor_b")
	@Excel(name = "B相功率因数")
	private Double powerFactorB;

	@Field("power_factor_c")
	@Excel(name = "C相功率因数")
	private Double powerFactorC;

	@Field("rest_money")
	@Excel(name = "余额")
	private Double restMoney;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public String getElectricityType() {
		return electricityType;
	}

	public void setElectricityType(String electricityType) {
		this.electricityType = electricityType;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getVoltageA() {
		return voltageA;
	}

	public void setVoltageA(Double voltageA) {
		this.voltageA = voltageA;
	}

	public Double getVoltageB() {
		return voltageB;
	}

	public void setVoltageB(Double voltageB) {
		this.voltageB = voltageB;
	}

	public Double getVoltageC() {
		return voltageC;
	}

	public void setVoltageC(Double voltageC) {
		this.voltageC = voltageC;
	}

	public Double getVoltageAB() {
		return voltageAB;
	}

	public void setVoltageAB(Double voltageAB) {
		this.voltageAB = voltageAB;
	}

	public Double getVoltageBC() {
		return voltageBC;
	}

	public void setVoltageBC(Double voltageBC) {
		this.voltageBC = voltageBC;
	}

	public Double getVoltageCA() {
		return voltageCA;
	}

	public void setVoltageCA(Double voltageCA) {
		this.voltageCA = voltageCA;
	}

	public Double getCurrent() {
		return current;
	}

	public void setCurrent(Double current) {
		this.current = current;
	}

	public Double getCurrentA() {
		return currentA;
	}

	public void setCurrentA(Double currentA) {
		this.currentA = currentA;
	}

	public Double getCurrentB() {
		return currentB;
	}

	public void setCurrentB(Double currentB) {
		this.currentB = currentB;
	}

	public Double getCurrentC() {
		return currentC;
	}

	public void setCurrentC(Double currentC) {
		this.currentC = currentC;
	}

	public Double getKw() {
		return kw;
	}

	public void setKw(Double kw) {
		this.kw = kw;
	}

	public Double getKwA() {
		return kwA;
	}

	public void setKwA(Double kwA) {
		this.kwA = kwA;
	}

	public Double getKwB() {
		return kwB;
	}

	public void setKwB(Double kwB) {
		this.kwB = kwB;
	}

	public Double getKwC() {
		return kwC;
	}

	public void setKwC(Double kwC) {
		this.kwC = kwC;
	}

	public Double getKvar() {
		return kvar;
	}

	public void setKvar(Double kvar) {
		this.kvar = kvar;
	}

	public Double getKvarA() {
		return kvarA;
	}

	public void setKvarA(Double kvarA) {
		this.kvarA = kvarA;
	}

	public Double getKvarB() {
		return kvarB;
	}

	public void setKvarB(Double kvarB) {
		this.kvarB = kvarB;
	}

	public Double getKvarC() {
		return kvarC;
	}

	public void setKvarC(Double kvarC) {
		this.kvarC = kvarC;
	}

	public Double getKva() {
		return kva;
	}

	public void setKva(Double kva) {
		this.kva = kva;
	}

	public Double getKvaA() {
		return kvaA;
	}

	public void setKvaA(Double kvaA) {
		this.kvaA = kvaA;
	}

	public Double getKvaB() {
		return kvaB;
	}

	public void setKvaB(Double kvaB) {
		this.kvaB = kvaB;
	}

	public Double getKvaC() {
		return kvaC;
	}

	public void setKvaC(Double kvaC) {
		this.kvaC = kvaC;
	}

	public Double getKwh() {
		return kwh;
	}

	public void setKwh(Double kwh) {
		this.kwh = kwh;
	}

	public Double getKwhForward() {
		return kwhForward;
	}

	public void setKwhForward(Double kwhForward) {
		this.kwhForward = kwhForward;
	}

	public Double getKwhReverse() {
		return kwhReverse;
	}

	public void setKwhReverse(Double kwhReverse) {
		this.kwhReverse = kwhReverse;
	}

	public Double getKvarh1() {
		return kvarh1;
	}

	public void setKvarh1(Double kvarh1) {
		this.kvarh1 = kvarh1;
	}

	public Double getKvarh2() {
		return kvarh2;
	}

	public void setKvarh2(Double kvarh2) {
		this.kvarh2 = kvarh2;
	}

	public Double getPowerFactor() {
		return powerFactor;
	}

	public void setPowerFactor(Double powerFactor) {
		this.powerFactor = powerFactor;
	}

	public Double getPowerFactorA() {
		return powerFactorA;
	}

	public void setPowerFactorA(Double powerFactorA) {
		this.powerFactorA = powerFactorA;
	}

	public Double getPowerFactorB() {
		return powerFactorB;
	}

	public void setPowerFactorB(Double powerFactorB) {
		this.powerFactorB = powerFactorB;
	}

	public Double getPowerFactorC() {
		return powerFactorC;
	}

	public void setPowerFactorC(Double powerFactorC) {
		this.powerFactorC = powerFactorC;
	}

	public Double getRestMoney() {
		return restMoney;
	}

	public void setRestMoney(Double restMoney) {
		this.restMoney = restMoney;
	}

}
