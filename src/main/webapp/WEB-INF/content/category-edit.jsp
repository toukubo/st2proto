<!DOCTYPE html>
<%@taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta charset="utf-8">
<title>Category <s:property value="id" /></title>
</head>
<body>
 <div>
  <h1>
   Category
   <s:property value="id" />
  </h1>
 </div>

 <s:fielderror />

 <s:form method="post" action="%{#request.contextPath}/category/%{id}">
  <s:hidden name="_method" value="put" />
  <div>
   <label>ID</label>
   <div>
    <s:textfield id="id" name="id" disabled="true" />
   </div>
  </div>
  <div>
   <label>Name</label>
   <div>
    <s:textfield id="name" name="name" />
   </div>
  </div>
  <div>
   <s:submit />
  </div>
 </s:form>
 <a href="${pageContext.request.contextPath}/category"> <i></i> Back
  to Categories
 </a>
</body>
</html>
