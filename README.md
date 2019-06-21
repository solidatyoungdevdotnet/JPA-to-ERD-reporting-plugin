# JPA-to-ERD-reporting-plugin
This is a general plugin for creating a Vendor agnostic (Logical) ERD from JPA entities by reading the annotations.  
The resulting diagram should be logically correct even if not applicable to a given DB Vendor.  

## General features
- Supports Relationship detection with directed edges between nodes (crow's foot)
- based on graphviz so the dot layout script is easily pulled from the report
- all non-transient, non-static, JPA annotated fields including (Id, Column, JoinColumn)
- datatype detection
- Primary and Foriegn key detection

## Coming soon
- new license (I'm leaning toward BSD but I need to check all the dependencies first)
- Clean up the actual report layout 
- refactor the code.  The code is fugly and I don't wan't future employers to look at it
- Code coverage
- Build a sample reference project
- document config