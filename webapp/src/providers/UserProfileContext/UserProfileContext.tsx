import {createContext} from "react";
import {UserProfile} from "../../types/UserProfile";

type UserProfileContextValue = {
  userProfile: UserProfile | undefined;
  setUserProfile: (up?: UserProfile) => void;
  isPending: boolean;
  error: Error | null;
};

export const UserProfileContext = createContext<UserProfileContextValue>({
  userProfile: undefined,
  setUserProfile: () => {},
  isPending: false,
  error: null,
});
