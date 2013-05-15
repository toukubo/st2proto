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

 <s:form method="post" action="%{#pageContext.request.contextPath}category/%{id}">
  <s:hidden name="_method" value="put" />
  <s:hidden id="id" name="id" />
  <s:textfield id="name" label="Name" name="name" />
  <s:submit />
 </s:form>
 <a href="${pageContext.request.contextPath}/category"> <i></i> Back
  to Categories
 </a>
</body>
</html>
