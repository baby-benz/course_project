# course_project
Fourth course year project

===
Server Layer for android clients
- Database
    - users
        - name
        - surname
        - table number from isu (UID)
        - roles (enum: customer, employee)
    - orders
        - status
        - List of products in order
    - products
        - name
        - available
    - building
        - short name of building
        - address (optional)