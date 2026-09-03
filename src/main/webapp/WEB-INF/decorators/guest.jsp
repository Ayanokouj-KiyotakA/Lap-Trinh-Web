<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><sitemesh:write property='title' /> - Bài tập 04</title>
	<link rel="stylesheet"
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
	<sitemesh:write property='head' />
</head>
<body class="bg-light">
	<div class="container d-flex justify-content-center align-items-center" style="min-height:100vh">
		<div class="col-12 col-sm-8 col-md-5 col-lg-4">
			<div class="card shadow-sm">
				<div class="card-body p-4">
					<sitemesh:write property='body' />
				</div>
			</div>
		</div>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
