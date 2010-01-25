
	<input type="hidden" id="userId" name="userId" value="${userInfo.userId!}"/>

	<tr>	
		<td class="tdLabel">
			UserInfo.ALIAS_USERNAME:
		</td>		
		<td>
		<input value="${userInfo.username!}" name="username" class="" maxlength="50" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			UserInfo.ALIAS_PASSWORD:
		</td>		
		<td>
		<input value="${userInfo.password!}" name="password" class="" maxlength="50" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			UserInfo.ALIAS_BIRTH_DATE:
		</td>		
		<td>
		<input value="${userInfo.birthDateString!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" id="birthDateString" name="birthDateString"  maxlength="0" class="" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			UserInfo.ALIAS_SEX:
		</td>		
		<td>
		<input value="${userInfo.sex!}" name="sex" class="validate-integer max-value-127" maxlength="4" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			UserInfo.ALIAS_AGE:
		</td>		
		<td>
		<input value="${userInfo.age!}" name="age" class="validate-integer max-value-2147483647" maxlength="11" />
		</td>
	</tr>	
	
		