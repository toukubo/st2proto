<!DOCTYPE html>
<%@taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta charset="utf-8">
<title>New Category</title>
</head>
<body>
 <div>
  <h1>New Category</h1>
 </div>

 <s:fielderror cssClass="alert alert-error" />

 <s:form method="post" action="%{#request.contextPath}/category"
  cssClass="form-horizontal" theme="simple">
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
