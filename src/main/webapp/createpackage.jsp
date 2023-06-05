<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Create Package</title>
    <!-- Include necessary CSS and JavaScript files -->
    <link rel="stylesheet" type="text/css" href="css/createpackage.css">

    <script src="your-javascript-file.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment-with-locales.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <%--  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />--%>
    <script>
        $(document).ready(function () {

            $('form').submit(function (event) {
                // event.preventDefault();

                // Get the raw departure datetime value
                var rawDepartureDatetime = $('#departure-datetime').val();
                console.log('Raw Departure datetime:', rawDepartureDatetime);

                // Get the raw arrival datetime value
                var rawArrivalDatetime = $('#arrival-datetime').val();
                console.log('Raw Arrival datetime:', rawArrivalDatetime);

                // Format departure datetime
                var departureDatetime = formatTimestamp(rawDepartureDatetime);
                console.log('Formatted Departure datetime:', departureDatetime);

                // Format arrival datetime
                var arrivalDatetime = formatTimestamp(rawArrivalDatetime);
                console.log('Formatted Arrival datetime:', arrivalDatetime);

                // Rest of the code...
            });

            // Function to format timestamp to required format (YYYY-MM-DD HH:mm:ss)
            function formatTimestamp(timestamp) {
                var date = new Date(timestamp);
                var year = date.getFullYear();
                var month = ('0' + (date.getMonth() + 1)).slice(-2);
                var day = ('0' + date.getDate()).slice(-2);
                var hours = ('0' + date.getHours()).slice(-2);
                var minutes = ('0' + date.getMinutes()).slice(-2);
                var seconds = ('0' + date.getSeconds()).slice(-2);

                return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
            }

            // Add flight
            $('#addFlight').click(function () {
                var flightCount = $('.flight').length + 1;
                var flightHtml = `
          <div class="flight section">
          <h3>Flight Information </h3>
          <label for="flightId">Flight ID:</label>
          <input type="text" id="flightId" name="flightId"><br>
          <label for="airline">Airline:</label>
          <input type="text" id="airline" name="airline"><br>
          <div class="section">
          <div class="datetime-container">
          <div class="datetime-option">
          <label for="departure-datetime">Departure:</label>
          <input type="datetime-local" id="departure-datetime" name="departure" class="datetimepicker">
          </div>
          </div>
          <div class="datetime-container">
          <div class="datetime-option">
          <label for="arrival-datetime">Arrival:</label>
          <input type="datetime-local" id="arrival-datetime" name="arrival" class="datetimepicker">
          </div>
          </div><br>
          <label for="flightPrice">Flight Price:</label>
          <input type="number" id="flightPrice" name="flightPrice" step="0.01"><br>
          </div>
          `;

                // Find the first flight and insert the new flight after it
                $('.flight').first().after(flightHtml);

                // $('.datetimepicker').daterangepicker({
                //     singleDatePicker: true,
                //     timePicker: true,
                //     timePicker24Hour: true,
                //     locale: {
                //         format: 'YYYY-MM-DD HH:mm:ss'
                //     }
                // });
                //
                // $('.datetimepicker').on('apply.daterangepicker', function (ev, picker) {
                //     $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss.SSSSSSSSS'));
                // });
            });

            // Add hotel
            $('#addHotel').click(function () {
                var hotelCount = $('.hotel').length + 1;
                var hotelHtml = `
          <div class="hotel section">
            <h3>Hotel Information </h3>
            <label for="hotelId">Hotel ID:</label>
            <input type="text" id="hotelId" name="hotelId"><br>
            <label for="hotelName">Hotel Name:</label>
            <input type="text" id="hotelName" name="hotelName"><br>
            <label for="hotelLocation">Hotel Location:</label>
            <input type="text" id="hotelLocation" name="hotelLocation"><br>
            <label for="hotelPrice">Hotel Price:</label>
            <input type="number" id="hotelPrice" name="hotelPrice" step="0.01"><br>
          </div>
        `;
                $('#hotelsContainer').append(hotelHtml);
            });

            // Add activity
            $('#addActivity').click(function () {
                var activityCount = $('.activity').length + 1;
                var activityHtml = `
          <div class="activity section">
            <h3>Activity Information </h3>
            <label for="activityId">Activity ID:</label>
            <input type="text" id="activityId" name="activityId"><br>
            <label for="activityName">Activity Name:</label>
            <input type="text" id="activityName" name="activityName"><br>
            <label for="activityDescription">Activity Description:</label>
            <textarea id="activityDescription" name="activityDescription"></textarea><br>
            <label for="activityPrice">Activity Price:</label>
            <input type="number" id="activityPrice" name="activityPrice" step="0.01"><br>
          </div>
        `;
                $('#activitiesContainer').append(activityHtml);
            });
        });
    </script>
</head>
<body>

<jsp:include page="menu.jsp" />

<h2>Create Package</h2>

<form action="CreatePackageServlet" method="POST">

    <!-- Package Information -->
    <div class="section">
        <h3>Package Information</h3>
        <label for="packageId">Package ID:</label>
        <input type="text" id="packageId" name="packageId" required><br>
        <label for="packageName">Package Name:</label>
        <input type="text" id="packageName" name="packageName" required><br>
        <label for="packageDescription">Package Description:</label>
        <textarea id="packageDescription" name="packageDescription" required></textarea><br>
        <label for="packagePrice">Package Price:</label>
        <input type="number" id="packagePrice" name="packagePrice" step="0.01" required><br>
    </div>

    <!-- Flight Information -->
    <div id="flightsContainer">
        <div class="section flight">
            <h3>Flight Information</h3>
            <label for="flightId">Flight ID:</label>
            <input type="text" id="flightId" name="flightId"><br>
            <label for="airline">Airline:</label>
            <input type="text" id="airline" name="airline"><br>
            <%--      <label for="departure"></label>--%>
            <%--      <input type="text" id="departure" name="departure" class="datetimepicker"><br>--%>
            <div class="section">
                <div class="datetime-container">
                    <div class="datetime-option">
                        <label for="departure-datetime">Departure:</label>
                        <input type="datetime-local" id="departure-datetime" name="departure" class="datetimepicker">
                    </div>
                </div>
                <div class="datetime-container">
                    <div class="datetime-option">
                        <label for="arrival-datetime">Arrival:</label>
                        <input type="datetime-local" id="arrival-datetime" name="arrival" class="datetimepicker">
                    </div>
                </div>
                <%--        <div class="apply-button">--%>
                <%--          <button type="button" id="applyButton">Apply</button>--%>
                <%--        </div>--%>
            </div>
            <label for="flightPrice">Flight Price:</label>
            <input type="number" id="flightPrice" name="flightPrice" step="0.01"><br>
        </div>
        <button type="button" id="addFlight">Add Flight</button>

        <!-- Hotel Information -->
        <div id="hotelsContainer">
            <div class="section hotel">
                <h3>Hotel Information 1</h3>
                <label for="hotelId">Hotel ID:</label>
                <input type="text" id="hotelId" name="hotelId"><br>
                <label for="hotelName">Hotel Name:</label>
                <input type="text" id="hotelName" name="hotelName"><br>
                <label for="hotelLocation">Hotel Location:</label>
                <input type="text" id="hotelLocation" name="hotelLocation"><br>
                <label for="hotelPrice">Hotel Price:</label>
                <input type="number" id="hotelPrice" name="hotelPrice" step="0.01"><br>
            </div>
        </div>
        <button type="button" id="addHotel">Add Hotel</button>

        <!-- Activity Information -->
        <div id="activitiesContainer">
            <div class="section activity">
                <h3>Activity Information 1</h3>
                <label for="activityId">Activity ID:</label>
                <input type="text" id="activityId" name="activityId"><br>
                <label for="activityName">Activity Name:</label>
                <input type="text" id="activityName" name="activityName"><br>
                <label for="activityDescription">Activity Description:</label>
                <textarea id="activityDescription" name="activityDescription"></textarea><br>
                <label for="activityPrice">Activity Price:</label>
                <input type="number" id="activityPrice" name="activityPrice" step="0.01"><br>
            </div>
        </div>
        <button type="button" id="addActivity">Add Activity</button>

        <input type="submit" value="Save Package">
    </div>

</form>

</body>
</html>
