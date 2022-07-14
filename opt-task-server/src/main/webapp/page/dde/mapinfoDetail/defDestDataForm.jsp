<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">

	<form id="defGoalDataFrom" class="defGoalDataFrom"
		onsubmit="return navTabSearch(this)"
		action="${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?&s_type=init"
		method="post">
		<input type="hidden" name="s_sourceUrl" value="${SOURCEURL}"/>
		<input type="hidden" name="s_soueceTableName" value="${param['s_soueceTableName']}"/>
		<input type="hidden" name="s_mapinfoId" value="${param['s_mapinfoId']}"/>
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label>数据库连接：</label>
				<select  name="s_goaldatabaseName" ref="tableNames" 
						refUrl="${pageContext.request.contextPath }/dde/mapInfoDetail!database.do?databaseName={value}">
					<option value="all">请选择数据库</option>
				    <c:forEach var="database" items="${DATABASE}">
      				  <option value="${database}" <c:if test="${goalDatabaseName eq database }">selected=selected</c:if>>${database}</option>
      			   </c:forEach>
				</select>
			</div>
			
			<div class="unit">
				<label>表名：</label>
				<input name="s_goalTableName" size="20"  value="${param['s_goalTableName']}" type="text" Class="required s_goalTableName" />
				<%-- <label style="width:60px">请选择：</label>
				<select class="combox table" name="s_goalTableName1" id="tableNames">
     				  <option value="all" >请选择表名</option>
     			   <c:forEach var="tables" items="${TABLES}">
      				  <option value="${tables}">${tables}</option>
      			   </c:forEach>
				</select> --%>
			</div>

			<div class="unit">
				<label>查询语句：</label>
				<s:textarea name="userdesc" cols="44" rows="5" />
			</div>
			
		</div>

		<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="button" onclick="submitMapInfo();">提交</button>
						</div>
					</div>
				</li>

				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div></li>
			</ul>
		</div>
	</form>
</div>

<script type="text/javascript">
	  function submitMapInfo(){
		  $("#defGoalDataFrom").submit();
		  $.pdialog.close($.pdialog.getCurrent());
	  }
	  $('.table', $.pdialog.getCurrent()).change(function(event){
		  $(".datadest").val($(this).val());
		  $(".s_goalTableName").val($(this).val());
		  $("#defGoalDataFrom").attr("action","${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?&s_type=reinitgoal")
		  		  
	  });
</script>