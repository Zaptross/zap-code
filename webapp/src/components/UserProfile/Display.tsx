import styled from "styled-components";
import useFetchUserProfile from "../../hooks/fetchUserProfile";
import UserProfile from "./UserProfile";

const Spacer = styled.span`
  margin-right: 1rem;
`;

const RS = styled.span`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-items: space-between;
  :first-child {
    margin-right: 1rem;
  }
`;

export default function UserProfileDisplay() {
  const fetchUserProfile = useFetchUserProfile();
  const {isPending, data, error} = fetchUserProfile();

  if (isPending) {
    return <UserProfile userName="Loading..." userAvatar="" />;
  }

  if (error || !data) {
    return (
      <Spacer>
        {/* // TODO - use env for the URL */}
        <a href="http://localhost:8080/auth/login">Login</a>
      </Spacer>
    );
  }

  return (
    <RS>
      {/* // TODO - use env for the URL */}
      <a href="http://localhost:8080/auth/logout">Logout</a>
      <UserProfile userName={data.username} userAvatar={data.avatarUrl} />
    </RS>
  );
}
