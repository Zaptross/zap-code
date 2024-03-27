import styled from "styled-components";

type LinesDisplayProps = {
  logs: string[];
};

const Div = styled.div`
  display: flex;
  flex-direction: column;

  padding: 0.5rem;

  p {
    padding: 0.5rem;
    padding-left: 0.75rem;
    margin: 0;
  }

  :nth-child(even) {
    background-color: #292929;
    color: #dfdfdf;
  }
  :nth-child(odd) {
    background-color: #343434;
    color: #dfdfdf;
  }
  > :hover {
    background-color: #3d3d3d;
  }
`;

export default function LinesDisplay({logs}: LinesDisplayProps) {
  return (
    <Div>
      <div>
        {logs.map((line, index) => (
          <p key={index}>{line}</p>
        ))}
      </div>
    </Div>
  );
}
