<!DOCTYPE html>
<%@taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Category ${id}</title>
</head>
<body>
  <div>
    <h1>Category ${id}</h1>
  </div>
  <table>
    <tr>
      <td>ID</td>
      <td><s:property value="id"/></td>
    </tr>
    <tr>
      <td>Name</td>
      <td><s:property value="name"/></td>
    </tr>
  </table>
  <a href="${pageContext.request.contextPath}/category">
  <i></i> Back to Category
  </a>
</body>
</html>
	
