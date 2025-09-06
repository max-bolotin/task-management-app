# TODO List - Task Management App

## High Priority

- [ ] **Auto-assign project owner from JWT token** - When authentication is implemented,
  automatically set the logged-in user as project owner (like Jira)
- [ ] **Implement JWT authentication** - Add security context to get current user ID
- [ ] **Add user service integration** - Validate that ownerId exists in user service

## Medium Priority

- [ ] **Add project update (PATCH) endpoint**
- [ ] **Add task assignment validation** - Check if assigneeId exists in user service
- [ ] **Add task status transition validation** - Implement proper workflow rules
- [ ] **Add pagination for project/task lists**

## Low Priority

- [ ] **Add search functionality**
- [ ] **Add task comments**
- [ ] **Add project permissions/roles**