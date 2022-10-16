const isValidWagerInput = (input: string): boolean => {
  return (
    input == "0" ||
    (new RegExp(/^(|[1-9]\d*)$/).test(input) && input.length <= 4)
  );
};

export default isValidWagerInput;
