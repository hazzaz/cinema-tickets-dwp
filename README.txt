1. For logging, apache common logging utility with log4j is utilised. Log4j properties file in the the resources directory.

2. TicketTypeRequest objects has been made immutable by making the class and its fields final, in addition its toString() method has been over ridden for debug purpose.

3. To handle invalid purchases cases, InvalidPurchaseException class takes in a status code depending on which invalid purchase case it is.

4. For mock and unit testing, Mockito and JUnit testing has been used respectively, covering the following scenarios:
a. Total price for both a success and fail case with different inputs
b. Total number of tickets for both a success and fail case with different inputs
c. Total number of seats for both a success and fail case with different inputs
d. Invalid purchase case of 0 adult tickets with any number of child and infants and checking the exception message and code
e. Invalid purchase case of exceeding the maximum number of tickets and checking the exception message and code
f. Invalid purchase case of an account id with value 0 or negative and checking the exception message and code
g. Verify invocations to the ticket payment service
h. Verify invocations to the seat reservation service

5. Tests can be run either within the IDE or from commandline > mvn [TestName]

